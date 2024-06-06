//package com.example.demo.controller;
//
//
//import com.example.demo.db.entity.MedicineEntity;
//import com.example.demo.service.MedicineService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.io.IOException;
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class MedicineController {
//    private final MedicineService medicineService;
//    private List<MedicineEntity> currentMedicines;
//
//    @GetMapping("/")
//    public String index(Model model) {
//        currentMedicines = medicineService.findAllMedicines();
//        model.addAttribute("medicines", currentMedicines);
//        return "index";
//    }
//
//
//    @GetMapping("/medicines/sort")
//    public String sortMedicinesList(@RequestParam String sortBy, Model model) {
//        if (currentMedicines == null) {
//            currentMedicines = medicineService.findAllMedicines();
//        }
//        currentMedicines = medicineService.sortMedicines(currentMedicines, sortBy);
//        model.addAttribute("medicines", currentMedicines);
//        return "index";
//    }
//
//    @GetMapping("/medicines/filter")
//    public String filterMedicinesList(@RequestParam String filterBy, String filterText, Model model) {
//        if (currentMedicines == null) {
//            currentMedicines = medicineService.findAllMedicines();
//        }
//
//        currentMedicines = medicineService.filterMedicines(currentMedicines, filterBy, filterText);
//        model.addAttribute("medicines", currentMedicines);
//        return "index";
//    }
//
//    @GetMapping("/random")
//    public ModelAndView randomMedicine() {
//        MedicineEntity randomMedicine = medicineService.getRandomMedicine();
//
//        if (randomMedicine != null) {
//            return new ModelAndView("redirect:/medicines/" + randomMedicine.getId());
//        } else {
//            return new ModelAndView("redirect:/");
//        }
//    }
//
//    @GetMapping("/medicines/{id}")
//    public ModelAndView MedicineDetails(@PathVariable("id") Long id) {
//        MedicineEntity medicine = medicineService.getMedicineById(id);
//
//        ModelAndView modelAndView = new ModelAndView("medicineDetails");
//
//        modelAndView.addObject("medicine", medicine);
//
//        return modelAndView;
//    }
//
//    @GetMapping("/medicineStock")
//    public ModelAndView listOrders(Model model) {
//        ModelAndView modelAndView = new ModelAndView("medicinesStock");
//
//        List<MedicineEntity> allMedicines = medicineService.findAllMedicines();
//        modelAndView.addObject("medicines", allMedicines);
//        return modelAndView;
//    }
//
//    @GetMapping("/addMedicine")
//    public ModelAndView addMedicine() {
//        MedicineEntity medicine = new MedicineEntity();
//        ModelAndView modelAndView = new ModelAndView("addMedicine");
//        modelAndView.addObject("medicine", medicine);
//        return modelAndView;
//    }
//
//    @PostMapping("/saveMedicine")
//    public String addMedicine(@ModelAttribute("medicine") MedicineEntity medicine, @RequestParam(
//            "image") MultipartFile image) {
//        try {
//                if (!image.isEmpty()) {
//                    medicine.setCover(image.getBytes());
//                }
//            medicineService.saveMedicine(medicine);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return "redirect:/";
//    }
//
//    @GetMapping("/editMedicine/{id}")
//    public ModelAndView editMedicineForm(@PathVariable(value = "id") long id) {
//        MedicineEntity medicine = medicineService.getMedicineById(id);
//        ModelAndView modelAndView = new ModelAndView("editMedicine");
//        modelAndView.addObject("medicine", medicine);
//        return modelAndView;
//    }
//
//    @PostMapping("/editMedicine")
//    public String editMedicine(@ModelAttribute MedicineEntity medicine,
//                           @RequestParam("image") MultipartFile image) {
//        try {
//            MedicineEntity existingMedicine = medicineService.getMedicineById(medicine.getId());
//            if (!image.isEmpty()) {
//                existingMedicine.setCover(image.getBytes());
//            }
//
//            existingMedicine.setMedicineName(medicine.getMedicineName());
//            existingMedicine.setMedicineDetails(medicine.getMedicineDetails());
//            existingMedicine.setStockCount(medicine.getStockCount());
//
//            medicineService.saveMedicine(existingMedicine);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "redirect:/editMedicine/" + medicine.getId() + "?error";
//        }
//        return "redirect:/Medicines/" + medicine.getId();
//    }
//
//    @GetMapping(value="/deleteMedicine/{id}")
//    public String deleteMedicine(@PathVariable Long id) {
//        medicineService.deleteMedicineById(id);
//        return "redirect:/";
//    }
//
//}
