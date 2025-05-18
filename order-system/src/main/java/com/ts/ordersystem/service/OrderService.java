package com.ts.ordersystem.service;

import com.ts.ordersystem.entity.Order;
import com.ts.ordersystem.event.OrderEvent;
import com.ts.ordersystem.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final RestTemplate restTemplate;
    
    @Transactional
    public Order createOrder(Long goodsId, Integer quantity, Long price) {
        // 1. 주문 생성
        Order order = new Order();
        order.setGoodsId(goodsId);
        order.setQuantity(quantity);
        order.setTotalPrice(price * quantity);
        order = orderRepository.save(order);
        
        try {
            // 2. 재고 감소 요청
            String goodsServiceUrl = "http://GOODS-SERVICE/goods/" + goodsId + "/decrease?quantity=" + quantity;
            restTemplate.postForObject(goodsServiceUrl, null, Void.class);
            
            // 3. 주문 상태 업데이트
            order.setStatus(Order.OrderStatus.CONFIRMED);
            order = orderRepository.save(order);
            
            // 4. 주문 생성 이벤트 발행
            eventPublisher.publishEvent(OrderEvent.createOrderCreated(order.getId(), goodsId, quantity));
            
            return order;
        } catch (Exception e) {
            // 5. 실패 시 주문 취소
            order.setStatus(Order.OrderStatus.FAILED);
            orderRepository.save(order);
            
            // 6. 주문 취소 이벤트 발행
            eventPublisher.publishEvent(OrderEvent.createOrderCancelled(order.getId(), goodsId, quantity));
            
            throw new RuntimeException("주문 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    @Transactional
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다: " + orderId));
    }
} 