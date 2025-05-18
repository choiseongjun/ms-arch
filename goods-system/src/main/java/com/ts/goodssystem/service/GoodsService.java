package com.ts.goodssystem.service;

import com.ts.goodssystem.entity.Goods;
import com.ts.goodssystem.exception.GoodsException;
import com.ts.goodssystem.repository.GoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GoodsService {
    
    private final GoodsRepository goodsRepository;
    
    @Transactional
    public void decreaseStock(Long goodsId, int quantity) {
        Goods goods = goodsRepository.findByIdWithPessimisticLock(goodsId)
                .orElseThrow(() -> new GoodsException.GoodsNotFoundException(goodsId));
                
        if (goods.getStock() < quantity) {
            throw new GoodsException.InsufficientStockException(goodsId, quantity, goods.getStock());
        }
        
        goods.setStock(goods.getStock() - quantity);
        goodsRepository.save(goods);
    }
    
    @Transactional
    public void increaseStock(Long goodsId, int quantity) {
        Goods goods = goodsRepository.findByIdWithPessimisticLock(goodsId)
                .orElseThrow(() -> new GoodsException.GoodsNotFoundException(goodsId));
                
        goods.setStock(goods.getStock() + quantity);
        goodsRepository.save(goods);
    }
    
    @Transactional(readOnly = true)
    public Goods getGoods(Long goodsId) {
        return goodsRepository.findById(goodsId)
                .orElseThrow(() -> new GoodsException.GoodsNotFoundException(goodsId));
    }
} 