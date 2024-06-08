package com.example.demo.controller;

import com.example.demo.db.entity.CartItemEntity;
import com.example.demo.db.entity.MedicineEntity;
import com.example.demo.db.entity.OrderEntity;
import com.example.demo.db.entity.UserEntity;
import com.example.demo.db.repository.CartItemRepository;
import com.example.demo.service.MedicineService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import com.example.demo.util.DataValidation;
import com.example.demo.util.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private MedicineService medicineService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    CartItemRepository cartItemRepository;

    @GetMapping(value = "/order/{cartNumber}")
    public ModelAndView showOrderForm(@PathVariable Long cartNumber){
        ModelAndView modelAndView = new ModelAndView("confirmOrder");

        modelAndView.addObject("order", new OrderEntity());
        modelAndView.addObject("cartNumber", cartNumber);
        return modelAndView;
    }

    @PostMapping(value="/confirmOrder/{cartNumber}")
    @Transactional
    public ModelAndView submitOrder(@PathVariable Long cartNumber,
                                    @ModelAttribute("order") OrderEntity order,
                                    BindingResult result) {

        List<CartItemEntity> cartItems = cartItemRepository.findAllCartItemsByCartNumber(cartNumber);
        UserEntity receiver=userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (DataValidation.isValidPhoneNumber(order.getPhoneNumber())) {
            result.rejectValue("phoneNumber", "invalid.phone", "Невалиден телефонен номер!");}
        if (result.hasErrors()) {

            return new ModelAndView("confirmOrder",
                    "medicine",
                    cartItems);
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItemEntity cartItem : cartItems) {
            BigDecimal itemPrice = cartItem.getMedicine().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalPrice = totalPrice.add(itemPrice);
        }

        OrderEntity newOrder = new OrderEntity(
                order.getAddress(),
                LocalDate.now(),
                order.getPhoneNumber(),
                order.getCartItems(),
                receiver.getUsername(),
                totalPrice
        );
        orderService.saveOrder(newOrder);

        emailService.orderConfirmationEmail(
                receiver.getEmail(),
                receiver.getName(),
                cartItems.toString(),
                totalPrice,
                newOrder.getAddress(),
                newOrder.getPhoneNumber(),
                String.valueOf(newOrder.getDate()),
                String.valueOf(newOrder.getId())
        );

        for (CartItemEntity cartItem : cartItems) {
            cartItemRepository.delete(cartItem);
        }
        cartItems.clear();

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/listOrders")
    public ModelAndView listOrders() {
        return new ModelAndView("orders",
                "orders",
                orderService.findAllOrders());
    }

    @GetMapping("/deleteOrder/{id}")
    public ModelAndView deleteOrder(@PathVariable Long id){

        orderService.deleteOrderById(id);

        return new ModelAndView("redirect:/listOrders");
    }
}
