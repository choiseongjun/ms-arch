package com.ts.goodssystem.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ts.goodssystem.entity.Goods;
import com.ts.goodssystem.repository.GoodsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoodsEventListener {
    private final GoodsRepository goodsRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-events", groupId = "goods-service")
    @Transactional
    public void handleOrderEvent(String eventJson) {
        log.info("Received order event: {}", eventJson);
        
        try {
            // JSON을 Map으로 변환
            var event = objectMapper.readValue(eventJson, java.util.Map.class);
            String eventType = (String) event.get("eventType");
            Long goodsId = Long.valueOf(event.get("goodsId").toString());
            Integer quantity = Integer.valueOf(event.get("quantity").toString());
            
            switch (eventType) {
                case "ORDER_CREATED":
                    // 주문 생성 시 재고 감소
                    Goods goods = goodsRepository.findById(goodsId)
                            .orElseThrow(() -> new RuntimeException("Goods not found: " + goodsId));
                    
                    if (goods.getStock() < quantity) {
                        throw new RuntimeException("Insufficient stock for goods: " + goodsId);
                    }
                    
                    goods.setStock(goods.getStock() - quantity);
                    goodsRepository.save(goods);
                    log.info("Decreased stock for goods: {}, new stock: {}", goodsId, goods.getStock());
                    break;
                    
                case "ORDER_CANCELLED":
                    // 주문 취소 시 재고 증가
                    goods = goodsRepository.findById(goodsId)
                            .orElseThrow(() -> new RuntimeException("Goods not found: " + goodsId));
                    
                    goods.setStock(goods.getStock() + quantity);
                    goodsRepository.save(goods);
                    log.info("Increased stock for goods: {}, new stock: {}", goodsId, goods.getStock());
                    break;
            }
        } catch (Exception e) {
            log.error("Error processing order event: {}", eventJson, e);
            throw new RuntimeException("Failed to process order event", e);
        }
    }
} 