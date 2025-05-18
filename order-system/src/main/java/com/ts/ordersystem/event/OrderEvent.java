package com.ts.ordersystem.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private Long orderId;
    private Long goodsId;
    private Integer quantity;
    private String eventType;
    
    public enum EventType {
        ORDER_CREATED,    // 주문 생성됨
        ORDER_CONFIRMED,  // 주문 확인됨
        ORDER_CANCELLED,  // 주문 취소됨
        ORDER_COMPLETED   // 주문 완료됨
    }
    
    public static OrderEvent createOrderCreated(Long orderId, Long goodsId, Integer quantity) {
        return new OrderEvent(orderId, goodsId, quantity, EventType.ORDER_CREATED.name());
    }
    
    public static OrderEvent createOrderConfirmed(Long orderId, Long goodsId, Integer quantity) {
        return new OrderEvent(orderId, goodsId, quantity, EventType.ORDER_CONFIRMED.name());
    }
    
    public static OrderEvent createOrderCancelled(Long orderId, Long goodsId, Integer quantity) {
        return new OrderEvent(orderId, goodsId, quantity, EventType.ORDER_CANCELLED.name());
    }
    
    public static OrderEvent createOrderCompleted(Long orderId, Long goodsId, Integer quantity) {
        return new OrderEvent(orderId, goodsId, quantity, EventType.ORDER_COMPLETED.name());
    }
} 