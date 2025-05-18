package com.ts.ordersystem.event;

import com.ts.ordersystem.entity.Order;
import com.ts.ordersystem.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final OrderRepository orderRepository;

    @EventListener
    @Transactional
    public void handleOrderEvent(OrderEvent event) {
        log.info("Received order event: {}", event);
        
        try {
            switch (OrderEvent.EventType.valueOf(event.getEventType())) {
                case ORDER_CREATED:
                    // 주문이 생성되면 주문 상태를 PENDING으로 변경
                    Order order = orderRepository.findById(event.getOrderId())
                            .orElseThrow(() -> new RuntimeException("Order not found: " + event.getOrderId()));
                    order.setStatus(Order.OrderStatus.PENDING);
                    orderRepository.save(order);
                    break;
                    
                case ORDER_CONFIRMED:
                    // 주문이 확인되면 주문 상태를 CONFIRMED로 변경
                    order = orderRepository.findById(event.getOrderId())
                            .orElseThrow(() -> new RuntimeException("Order not found: " + event.getOrderId()));
                    order.setStatus(Order.OrderStatus.CONFIRMED);
                    orderRepository.save(order);
                    break;
                    
                case ORDER_CANCELLED:
                    // 주문이 취소되면 주문 상태를 CANCELLED로 변경
                    order = orderRepository.findById(event.getOrderId())
                            .orElseThrow(() -> new RuntimeException("Order not found: " + event.getOrderId()));
                    order.setStatus(Order.OrderStatus.CANCELLED);
                    orderRepository.save(order);
                    break;
                    
                case ORDER_COMPLETED:
                    // 주문이 완료되면 주문 상태를 COMPLETED로 변경
                    order = orderRepository.findById(event.getOrderId())
                            .orElseThrow(() -> new RuntimeException("Order not found: " + event.getOrderId()));
                    order.setStatus(Order.OrderStatus.COMPLETED);
                    orderRepository.save(order);
                    break;
            }
        } catch (Exception e) {
            log.error("Error processing order event: {}", event, e);
            throw new RuntimeException("Failed to process order event", e);
        }
    }

    @KafkaListener(topics = "order-events", groupId = "order-service")
    @Transactional
    public void handleKafkaOrderEvent(OrderEvent event) {
        // Kafka로부터 받은 이벤트도 동일한 로직으로 처리
        handleOrderEvent(event);
    }
} 