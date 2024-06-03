package ru.kozarez.websiteoperatorrates.appstart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kozarez.websiteoperatorrates.services.RateService;

@Component
public class AppStartupRunner implements CommandLineRunner {
    @Autowired
    private RateService rateService;

    public void run(String... args) {
        rateService.parseTele2Rates();
        rateService.parseMegafonRates();
        rateService.parseBeelineRates();
        rateService.parseRostelekomRates();
        rateService.parseTinkoffRates();
    }
}
