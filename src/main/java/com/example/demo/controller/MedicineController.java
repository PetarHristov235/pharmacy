package com.example.demo.controller;


import com.example.demo.db.entity.CartItemEntity;
import com.example.demo.db.entity.MedicineEntity;
import com.example.demo.db.entity.UserEntity;
import com.example.demo.db.repository.CartItemRepository;
import com.example.demo.service.CartItemService;
import com.example.demo.service.MedicineService;
import com.example.demo.service.UserService;
import com.example.demo.util.DataValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;
    private final UserService userService;
    private final CartItemService cartItemService;
    private final CartItemRepository cartItemRepository;
    private List<MedicineEntity> currentMedicines;
    private List<CartItemEntity> cartItems = new ArrayList<>();
    private Long cartNumber;

    @GetMapping("/")
    public String index(Model model) {
        currentMedicines = medicineService.findAllMedicines();
        medicineService.sortMedicines(currentMedicines,"id");

        model.addAttribute("medicines", currentMedicines);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity authenticatedUser = userService.getUserByUsername(username);
        if (authenticatedUser != null) {
            cartNumber = authenticatedUser.getCartNumber();
            cartItems = cartItemRepository.findAllCartItemsByCartNumber(cartNumber);
        }
        model.addAttribute("cartItems", cartItems);

        return "index";
    }


    @GetMapping("/medicines/sort")
    public String sortMedicinesList(@RequestParam String sortBy, Model model) {
        currentMedicines = medicineService.sortMedicines(currentMedicines, sortBy);
        model.addAttribute("medicines", currentMedicines);
        model.addAttribute("cartItems", cartItems);
        return "index";
    }


    @GetMapping("/medicines/filter")
    public String filterMedicinesList(@RequestParam String filterBy, Model model) {
        currentMedicines = medicineService.filterMedicines(medicineService.findAllMedicines(), filterBy);
        model.addAttribute("medicines", currentMedicines);
        model.addAttribute("cartItems", cartItems);
        return "index";
    }


    @GetMapping("/medicines/search")
    public String searchMedicinesList(@RequestParam String searchText, Model model) {
        currentMedicines = medicineService.searchMedicines(medicineService.findAllMedicines(), searchText);
        model.addAttribute("medicines", currentMedicines);
        model.addAttribute("cartItems", cartItems);
        return "index";
    }

    @GetMapping("/medicines/{id}")
    public ModelAndView MedicineDetails(@PathVariable("id") Long id) {
        MedicineEntity medicine = medicineService.getMedicineById(id);

        ModelAndView modelAndView = new ModelAndView("medicineDetails");

        modelAndView.addObject("medicine", medicine);

        return modelAndView;
    }

    @GetMapping("/medicineStock")
    public ModelAndView listOrders() {
        ModelAndView modelAndView = new ModelAndView("medicineStock");

        List<MedicineEntity> allMedicines = medicineService.findAllMedicines();
        modelAndView.addObject("medicines", allMedicines);
        return modelAndView;
    }

    @GetMapping("/addMedicine")
    public ModelAndView addMedicine() {
        MedicineEntity medicine = new MedicineEntity();
        ModelAndView modelAndView = new ModelAndView("addMedicine");
        modelAndView.addObject("medicine", medicine);
        return modelAndView;
    }

    @PostMapping("/saveMedicine")
    public ModelAndView addMedicine(@ModelAttribute("medicine") MedicineEntity medicine,
                                    @RequestParam("image") MultipartFile image,
                                    BindingResult result) {
        try{
            if (!image.isEmpty()) {
                medicine.setCover(image.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (DataValidation.isValidExpirationDate(medicine.getExpiryDate())) {
            result.rejectValue("expiryDate",
                    "invalid.expirationDate",
                    "Невалиден срок на годност!");

            return new ModelAndView("addMedicine", "medicine", medicine);
        }

        medicineService.saveMedicine(medicine);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/editMedicine/{id}")
    public ModelAndView editMedicineForm(@PathVariable(value = "id") long id) {
        MedicineEntity medicine = medicineService.getMedicineById(id);
        return new ModelAndView("editMedicine", "medicine", medicine);
    }


    @PostMapping("/editMedicine")
    public ModelAndView editMedicine(@ModelAttribute MedicineEntity medicine,
                                     BindingResult result,
                                     @RequestParam("image") MultipartFile image) {
        try {
            if (!image.isEmpty()) {
                medicine.setCover(image.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (DataValidation.isValidExpirationDate(medicine.getExpiryDate())) {
            result.rejectValue("expiryDate", "invalid.expirationDate", "Невалиден срок на годност!");
            return new ModelAndView("editMedicine").addObject("medicine", medicine).addObject("org.springframework.validation.BindingResult.medicine", result);
        }

        MedicineEntity existingMedicine = medicineService.getMedicineById(medicine.getId());

        existingMedicine.setMedicineName(medicine.getMedicineName());
        existingMedicine.setMedicineDetails(medicine.getMedicineDetails());
        existingMedicine.setStockCount(medicine.getStockCount());
        existingMedicine.setPrice(medicine.getPrice());
        existingMedicine.setExpiryDate(medicine.getExpiryDate());

        medicineService.saveMedicine(existingMedicine);
        return new ModelAndView("redirect:/medicines/" + medicine.getId());
    }


    @GetMapping(value = "/deleteMedicine/{id}")
    public String deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicineById(id);
        return "redirect:/";
    }

    @PostMapping("/addToCart/{amount}")
    public String addMedicineToCart(@ModelAttribute("medicineId") Long medicineId,
                                    @PathVariable Integer amount) {
        cartItems = cartItemService.addMedicineToCart(medicineId, amount, cartNumber);
        return "redirect:/";
    }
}
