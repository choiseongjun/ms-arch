package com.ts.goodssystem.exception;

public class GoodsException extends RuntimeException {
    public GoodsException(String message) {
        super(message);
    }

    public static class GoodsNotFoundException extends GoodsException {
        public GoodsNotFoundException(Long id) {
            super("Goods not found with id: " + id);
        }
    }

    public static class InsufficientStockException extends GoodsException {
        public InsufficientStockException(Long id, int requested, int available) {
            super(String.format("Insufficient stock for goods id: %d. Requested: %d, Available: %d", 
                    id, requested, available));
        }
    }
} 