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
    
    public static OrderEvent createOrderCreated(Long orderId, Long goodsId, Integer quantity) {
        return new OrderEvent(orderId, goodsId, quantity, "ORDER_CREATED");
    }
    
    public static OrderEvent createOrderCancelled(Long orderId, Long goodsId, Integer quantity) {
        return new OrderEvent(orderId, goodsId, quantity, "ORDER_CANCELLED");
    }
} 