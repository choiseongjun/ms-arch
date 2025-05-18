package com.ts.ordersystem.controller;

import com.ts.ordersystem.entity.Order;
import com.ts.ordersystem.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestParam Long goodsId,
            @RequestParam Integer quantity,
            @RequestParam Long price) {
        Order order = orderService.createOrder(goodsId, quantity, price);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrder(id);
        return ResponseEntity.ok(order);
    }
} 