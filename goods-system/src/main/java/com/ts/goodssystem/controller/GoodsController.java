package com.ts.goodssystem.controller;

import com.ts.goodssystem.common.ApiResponse;
import com.ts.goodssystem.entity.Goods;
import com.ts.goodssystem.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
    
    private final GoodsService goodsService;
    
    @GetMapping("/{id}")
    public ApiResponse<Goods> getGoods(@PathVariable Long id) {
        return ApiResponse.success(goodsService.getGoods(id));
    }
    
    @PostMapping("/{id}/decrease")
    public ApiResponse<Void> decreaseStock(@PathVariable Long id, @RequestParam int quantity) {
        goodsService.decreaseStock(id, quantity);
        return ApiResponse.success();
    }
    
    @PostMapping("/{id}/increase")
    public ApiResponse<Void> increaseStock(@PathVariable Long id, @RequestParam int quantity) {
        goodsService.increaseStock(id, quantity);
        return ApiResponse.success();
    }
} 