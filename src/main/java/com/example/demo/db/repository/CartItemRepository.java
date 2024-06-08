package com.example.demo.db.repository;

import com.example.demo.db.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    @Query(value = "SELECT cartItem FROM CartItemEntity cartItem where cartItem.cartNumber = " +
            ":cartNumber")
    List<CartItemEntity> findAllCartItemsByCartNumber(@Param(("cartNumber")) Long cartNumber);
}
