package com.ts.goodssystem.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvent {
    private Long goodsId;
    private Integer quantity;
    private String eventType;
    
    public enum EventType {
        STOCK_DECREASED,    // 재고 감소됨
        STOCK_INCREASED,    // 재고 증가됨
        STOCK_DECREASE_FAILED,  // 재고 감소 실패
        STOCK_INCREASE_FAILED   // 재고 증가 실패
    }
    
    public static GoodsEvent createStockDecreased(Long goodsId, Integer quantity) {
        return new GoodsEvent(goodsId, quantity, EventType.STOCK_DECREASED.name());
    }
    
    public static GoodsEvent createStockIncreased(Long goodsId, Integer quantity) {
        return new GoodsEvent(goodsId, quantity, EventType.STOCK_INCREASED.name());
    }
    
    public static GoodsEvent createStockDecreaseFailed(Long goodsId, Integer quantity) {
        return new GoodsEvent(goodsId, quantity, EventType.STOCK_DECREASE_FAILED.name());
    }
    
    public static GoodsEvent createStockIncreaseFailed(Long goodsId, Integer quantity) {
        return new GoodsEvent(goodsId, quantity, EventType.STOCK_INCREASE_FAILED.name());
    }
} 