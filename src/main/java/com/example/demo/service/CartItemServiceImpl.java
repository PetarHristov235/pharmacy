package com.example.demo.service;

import com.example.demo.db.entity.CartItemEntity;
import com.example.demo.db.entity.MedicineEntity;
import com.example.demo.db.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final MedicineService medicineService;

    @Override
    @Transactional
    public void emptyCart(Long cartId) {

    }

    @Override
    @Transactional
    public List<CartItemEntity> addMedicineToCart(Long medicineId, Integer amount,
                                                  Long cartNumber) {
        AtomicBoolean isExistingMedicine = new AtomicBoolean(false);
        MedicineEntity medicine = medicineService.getMedicineById(medicineId);

        List<CartItemEntity> cartItems = cartItemRepository.findAllCartItemsByCartNumber(cartNumber);

        cartItems.stream()
                .filter(cartItem -> cartItem.getMedicine().getId().equals(medicineId))
                .findFirst()
                .ifPresent(cartItem -> {
                    cartItem.setQuantity(cartItem.getQuantity() + amount);
                    medicine.setStockCount(medicine.getStockCount() - amount);
                    isExistingMedicine.set(true);
                });
        if (!isExistingMedicine.get()) {
            medicine.setStockCount(medicine.getStockCount() - amount);
            CartItemEntity cartItem = new CartItemEntity();
            cartItem.setMedicine(medicine);
            cartItem.setQuantity(amount);
            cartItem.setCartNumber(cartNumber);
            cartItem = cartItemRepository.save(cartItem);
            cartItems.add(cartItem);
        }
        return cartItems;
    }


    @Override
    public List<CartItemEntity> getAllCartItemsByCartNumber(Long cartNumber) {
        return cartItemRepository.findAllCartItemsByCartNumber(cartNumber);
    }
}
