package com.example.demo.controller;

import com.example.demo.db.entity.UserEntity;
import com.example.demo.service.UserService;
import com.example.demo.util.DataValidation;
import com.example.demo.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/loginProcessing")
    public String loginProcessing(@RequestParam("username") String username,
                                  @RequestParam("password") String password,
                                  Model model) {

        if (userService.existUserByUsernameAndPassword(username, password)) {
            return "redirect:/";

        } else {
            return "redirect:/login";
        }

    }


    @GetMapping("/register")
    public ModelAndView register(Model model) {
        return new ModelAndView("register",
                "user",
                new UserEntity());
    }

    @PostMapping("/registerProcessing")
    public ModelAndView registerProcessing(@ModelAttribute("user") UserEntity user, BindingResult result) {

        validateData(user, result);

        if (result.hasErrors()) {
            return new ModelAndView("register", "user", user);
        }

        userService.saveUser(user);
        emailService.registrationConfirmationEmail(user.getEmail(), user.getName());

        return new ModelAndView("redirect:/login");
    }

    private void validateData(UserEntity user, BindingResult result) {
        if (DataValidation.isValidUsername(user.getUsername())) {
            result.rejectValue("username",
                    "invalid.username",
                    "Невалидно потребителско име!");
        }

        if (DataValidation.isValidName(user.getName())) {
            result.rejectValue("name",
                    "invalid.firstName",
                    "Невалидно име!");
        }

        if (DataValidation.isValidEmail(user.getEmail())) {
            result.rejectValue("email",
                    "invalid.email",
                    "Невалиден имейл!");
        }

        if (DataValidation.isValidPassword(user.getPassword())) {
            result.rejectValue("password",
                    "invalid.password",
                    "Невалидна парола!");
        }
        if (userService.existsUserEmail(user.getEmail())) {
            result.rejectValue("email",
                    "duplicate.email",
                    "Имейл е зает!");
        }
        if (userService.existsUsername(user.getUsername())) {
            result.rejectValue("username",
                    "duplicate.username",
                    "Потребителското име е заето!");
        }
    }
}