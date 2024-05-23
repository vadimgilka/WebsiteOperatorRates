package ru.kozarez.websiteoperatorrates.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kozarez.websiteoperatorrates.entities.RateEntity;
import ru.kozarez.websiteoperatorrates.services.RateService;

import java.util.List;

@Controller
@AllArgsConstructor
public class RateController {
    private final RateService rateService;

    @GetMapping("/")
    public String index(
            @RequestParam(required = false) Integer priceFrom,
            @RequestParam(required = false) Integer priceTo,
            @RequestParam(required = false) Integer gbFrom,
            @RequestParam(required = false) Integer gbTo,
            @RequestParam(required = false) Integer minutesFrom,
            @RequestParam(required = false) Integer minutesTo,
            @RequestParam(required = false) Integer messagesFrom,
            @RequestParam(required = false) Integer messagesTo,
            Model model) {

        List<RateEntity> rates = rateService.getFilteredRates(priceFrom, priceTo, gbFrom, gbTo, minutesFrom, minutesTo, messagesFrom, messagesTo);
        model.addAttribute("rates", rates);
        model.addAttribute("priceFrom", priceFrom != null ? priceFrom : "");
        model.addAttribute("priceTo", priceTo != null ? priceTo : "");
        model.addAttribute("gbFrom", gbFrom != null ? gbFrom : "");
        model.addAttribute("gbTo", gbTo != null ? gbTo : "");
        model.addAttribute("minutesFrom", minutesFrom != null ? minutesFrom : "");
        model.addAttribute("minutesTo", minutesTo != null ? minutesTo : "");
        model.addAttribute("messagesFrom", messagesFrom != null ? messagesFrom : "");
        model.addAttribute("messagesTo", messagesTo != null ? messagesTo : "");
        return "index";
    }
}
