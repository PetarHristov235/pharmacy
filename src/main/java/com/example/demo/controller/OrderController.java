package com.example.demo.controller;

import com.example.demo.db.entity.BookEntity;
import com.example.demo.db.entity.OrderEntity;
import com.example.demo.db.entity.UserEntity;
import com.example.demo.service.BookService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import com.example.demo.util.DataValidation;
import com.example.demo.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/orderBook/{id}")
    public ModelAndView order(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("confirmOrder");

        modelAndView.addObject("book", bookService.getBookById(id));
        modelAndView.addObject("order", new OrderEntity());

        return modelAndView;
    }

    @PostMapping(value="/confirmOrder/{id}")
    public ModelAndView submitOrder(@PathVariable Long id,
                                    @ModelAttribute("order") OrderEntity order,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {

        BookEntity book=bookService.getBookById(id);
        UserEntity receiver=userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!DataValidation.isValidPhoneNumber(order.getPhoneNumber())) {
            result.rejectValue("phoneNumber", "invalid.phone", "Invalid phone number!");}
        if (result.hasErrors()) {

            return new ModelAndView("confirmOrder",
                    "book",
                    book);
        }

        OrderEntity newOrder = new OrderEntity(
                order.getAddress(),
                LocalDate.now(),
                order.getPhoneNumber(),
                book.getBookName(),
                receiver.getUsername()
        );
        orderService.saveOrder(newOrder);

        emailService.orderConfirmationEmail(
                receiver.getEmail(),
                receiver.getName(),
                book.getBookName(),
                newOrder.getAddress(),
                newOrder.getPhoneNumber(),
                String.valueOf(newOrder.getDate()),
                String.valueOf(newOrder.getId())
        );

        bookService.decreaseBookStockCount(book);

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/listOrders")
    public ModelAndView listOrders(Model model) {
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
