package com.example.demo.controller;

import com.example.demo.db.entity.MedicineEntity;
import com.example.demo.db.entity.OrderEntity;
import com.example.demo.db.entity.UserEntity;
import com.example.demo.service.MedicineService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import com.example.demo.util.DataValidation;
import com.example.demo.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

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

    @GetMapping(value = "/orderBook/{id}")
    public ModelAndView order(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("confirmOrder");

        modelAndView.addObject("medicine", medicineService.getMedicineById(id));
        modelAndView.addObject("order", new OrderEntity());

        return modelAndView;
    }

    @PostMapping(value="/confirmOrder/{id}")
    public ModelAndView submitOrder(@PathVariable Long id,
                                    @ModelAttribute("order") OrderEntity order,
                                    BindingResult result) {

        MedicineEntity medicine= medicineService.getMedicineById(id);
        UserEntity receiver=userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!DataValidation.isValidPhoneNumber(order.getPhoneNumber())) {
            result.rejectValue("phoneNumber", "invalid.phone", "Invalid phone number!");}
        if (result.hasErrors()) {

            return new ModelAndView("confirmOrder",
                    "medicine",
                    medicine);
        }

        OrderEntity newOrder = new OrderEntity(
                order.getAddress(),
                LocalDate.now(),
                order.getPhoneNumber(),
                medicine.getMedicineName(),
                receiver.getUsername()
        );
        orderService.saveOrder(newOrder);

        emailService.orderConfirmationEmail(
                receiver.getEmail(),
                receiver.getName(),
                medicine.getMedicineName(),
                newOrder.getAddress(),
                newOrder.getPhoneNumber(),
                String.valueOf(newOrder.getDate()),
                String.valueOf(newOrder.getId())
        );

        medicineService.decreaseMedicineStockCount(medicine);

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

    @GetMapping("/remind/{id}")
    public ModelAndView remindDeadline(@PathVariable Long id){
        OrderEntity order=orderService.findOrderById(id);
        UserEntity user = userService.getUserByUsername(order.getUsername());

        emailService.orderDeadlineOverdueEmail(user.getEmail(),
                                                user.getName(),
                                                order.getTitle());

        return new ModelAndView("redirect:/listOrders");
    }
}
