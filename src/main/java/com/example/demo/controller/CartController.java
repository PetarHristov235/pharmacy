package com.example.demo.controller;

import com.example.demo.db.entity.CartItemEntity;
import com.example.demo.db.entity.MedicineEntity;
import com.example.demo.db.entity.UserEntity;
import com.example.demo.db.repository.CartItemRepository;
import com.example.demo.service.CartItemServiceImpl;
import com.example.demo.service.MedicineService;
import com.example.demo.service.UserServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    CartItemServiceImpl cartItemService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    MedicineService medicineService;


    private List<CartItemEntity> cartItems = new ArrayList<>();
    private Long cartNumber;

    @GetMapping("/reviewCart")
    @Transactional
    public String reviewCart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity authenticatedUser = userService.getUserByUsername(username);

        if (authenticatedUser != null) {
            cartNumber = authenticatedUser.getCartNumber();
            cartItems = cartItemRepository.findAllCartItemsByCartNumber(cartNumber);
            model.addAttribute("cartItems", cartItems);

            BigDecimal totalPrice = cartItems.stream()
                    .map(item -> item.getMedicine().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("cartNumber", cartNumber);
        }
        return "reviewCart";
    }

    @GetMapping("/removeFromCart/{cartItemId}")
    public String removeFromCart(@PathVariable Long cartItemId) {
        cartItemService.removeCartItemById(cartItemId);
        return "redirect:/reviewCart";
    }

    @GetMapping("/addOneToCart/{id}")
    public String addOneToCart(@PathVariable Long id) {
        CartItemEntity cartItem = cartItemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid cart item ID"));
        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItemRepository.save(cartItem);
        MedicineEntity medicine = medicineService.getMedicineById(cartItem.getMedicine().getId());
        medicine.setStockCount(medicine.getStockCount() - 1);
        medicineService.saveMedicine(medicine);
        return "redirect:/reviewCart";
    }

    @GetMapping("/removeOneFromCart/{id}")
    public String removeOneFromCart(@PathVariable Long id) {
        CartItemEntity cartItem = cartItemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid cart item ID"));
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(cartItem);
        }
        MedicineEntity medicine = medicineService.getMedicineById(cartItem.getMedicine().getId());
        medicine.setStockCount(medicine.getStockCount() + 1);
        medicineService.saveMedicine(medicine);
        return "redirect:/reviewCart";
    }
}
