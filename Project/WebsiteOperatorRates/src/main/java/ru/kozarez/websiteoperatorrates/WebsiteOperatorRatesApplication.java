package ru.kozarez.websiteoperatorrates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.kozarez.websiteoperatorrates.services.RateService;

import java.io.IOException;

@SpringBootApplication
public class WebsiteOperatorRatesApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(WebsiteOperatorRatesApplication.class, args);
	}

}
