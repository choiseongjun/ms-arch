package com.ts.ordersystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long goodsId;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private Long totalPrice;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = OrderStatus.CREATED;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum OrderStatus {
        CREATED,        // 주문 생성됨
        PENDING,        // 재고 확인 중
        CONFIRMED,      // 재고 확인 완료
        COMPLETED,      // 주문 완료
        CANCELLED,      // 주문 취소됨
        FAILED         // 주문 실패
    }
} 