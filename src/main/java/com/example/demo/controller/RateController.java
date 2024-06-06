//package com.example.demo.controller;
//
//import com.example.demo.db.entity.RateEntity;
//import com.example.demo.service.RateService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@Controller
//@RequiredArgsConstructor
//public class RateController {
//    private final RateService rateService;
//
//    @GetMapping("/rateMedicine/{id}")
//    ModelAndView rateMedicine(@PathVariable Long id, Model model) {
//        RateEntity rateEntity = new RateEntity();
//        rateEntity.setMedicineId(id);
//        model.addAttribute("rateEntity", rateEntity);
//        return new ModelAndView("rateMedicine");
//    }
//
//    @PostMapping("/saveRating")
//    public String saveRating(@ModelAttribute RateEntity rateEntity,
//                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//        if (rateEntity.getRate() == null) {
//            redirectAttributes.addFlashAttribute("error", "Моля, изберете оценка.");
//            return "redirect:/rateMedicine/" + rateEntity.getMedicineId();
//        }
//        rateService.saveRating(rateEntity);
//        return "redirect:/";
//    }
//
//    @GetMapping("/showRates/{id}")
//    ModelAndView showRates(@PathVariable Long id, Model model) {
//        model.addAttribute("rates", rateService.findRatesByMedicineId(id));
//        return new ModelAndView("showRates");
//    }
//}
