package com.ts.order.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    @EventListener
    public void handleOrderCreated(OrderEvent event) {
        if ("ORDER_CREATED".equals(event.getEventType())) {
            log.info("주문 생성 이벤트 발생: 주문ID={}, 상품ID={}, 수량={}", 
                    event.getOrderId(), event.getGoodsId(), event.getQuantity());
            // 여기에 주문 생성 후 필요한 추가 작업을 구현할 수 있습니다.
        }
    }

    @EventListener
    public void handleOrderCancelled(OrderEvent event) {
        if ("ORDER_CANCELLED".equals(event.getEventType())) {
            log.info("주문 취소 이벤트 발생: 주문ID={}, 상품ID={}, 수량={}", 
                    event.getOrderId(), event.getGoodsId(), event.getQuantity());
            // 여기에 주문 취소 후 필요한 추가 작업을 구현할 수 있습니다.
        }
    }
} 