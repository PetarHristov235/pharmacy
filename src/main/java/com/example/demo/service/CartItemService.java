package com.example.demo.service;

import com.example.demo.db.entity.CartItemEntity;

import java.util.List;

public interface CartItemService {
    void emptyCart(Long cartId);

    List<CartItemEntity> addMedicineToCart(Long medicineId, Integer amount, Long cartNumber);

    List<CartItemEntity> getAllCartItemsByCartNumber(Long cartNumber);
}
