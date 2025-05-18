package com.ts.goodssystem.repository;

import com.ts.goodssystem.entity.Goods;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
    
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT g FROM Goods g WHERE g.id = :id")
    Optional<Goods> findByIdWithOptimisticLock(@Param("id") Long id);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g FROM Goods g WHERE g.id = :id")
    Optional<Goods> findByIdWithPessimisticLock(@Param("id") Long id);
} 