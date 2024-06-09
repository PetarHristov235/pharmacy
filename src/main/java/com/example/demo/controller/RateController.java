package com.example.demo.controller;

import com.example.demo.db.entity.RateEntity;
import com.example.demo.service.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class RateController {
    private final RateService rateService;

    @GetMapping("/rateUs")
    ModelAndView rateForm() {
        RateEntity rateEntity = new RateEntity();
        return new ModelAndView("rateUs",
                "rate",
                rateEntity);
    }

    @PostMapping("/saveRating")
    public String saveRating(@ModelAttribute RateEntity rateEntity,
                             RedirectAttributes redirectAttributes) {
        if (rateEntity.getRate() == null) {
            redirectAttributes.addFlashAttribute("error", "Моля, изберете оценка.");
            return "redirect:/rateMedicine/" + rateEntity.getId();
        }
        rateService.saveRating(rateEntity);
        return "redirect:/";
    }

    @GetMapping("/showRates")
    ModelAndView showRates( Model model) {
        model.addAttribute("rates", rateService.findAll());
        return new ModelAndView("showRates");
    }
}
