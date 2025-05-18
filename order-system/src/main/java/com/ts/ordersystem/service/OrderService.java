package com.ts.ordersystem.service;

import com.ts.ordersystem.entity.Order;
import com.ts.ordersystem.event.OrderEvent;
import com.ts.ordersystem.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Order createOrder(Long goodsId, Integer quantity, Long price) {
        // 1. 주문 생성
        Order order = new Order();
        order.setGoodsId(goodsId);
        order.setQuantity(quantity);
        order.setTotalPrice(price * quantity);
        order = orderRepository.save(order);
        
        try {
            // 2. 주문 생성 이벤트 발행 (Spring Event)
            OrderEvent orderEvent = OrderEvent.createOrderCreated(order.getId(), goodsId, quantity);
            eventPublisher.publishEvent(orderEvent);
            
            // 3. Kafka로 이벤트 발행
            kafkaTemplate.send("order-events", orderEvent);
            log.info("Published order created event: {}", orderEvent);
            
            // 4. 재고 확인 후 주문 확인 이벤트 발행
            OrderEvent confirmEvent = OrderEvent.createOrderConfirmed(order.getId(), goodsId, quantity);
            eventPublisher.publishEvent(confirmEvent);
            kafkaTemplate.send("order-events", confirmEvent);
            log.info("Published order confirmed event: {}", confirmEvent);
            
            return order;
        } catch (Exception e) {
            // 5. 실패 시 주문 취소 이벤트 발행
            OrderEvent cancelEvent = OrderEvent.createOrderCancelled(order.getId(), goodsId, quantity);
            eventPublisher.publishEvent(cancelEvent);
            kafkaTemplate.send("order-events", cancelEvent);
            log.error("Failed to create order, published cancel event: {}", cancelEvent, e);
            
            throw new RuntimeException("주문 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다: " + orderId));
    }
} 