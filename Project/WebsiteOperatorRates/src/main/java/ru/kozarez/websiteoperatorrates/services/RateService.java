package ru.kozarez.websiteoperatorrates.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kozarez.websiteoperatorrates.dao.RateDAOInterface;
import ru.kozarez.websiteoperatorrates.entities.RateEntity;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RateService {
    @Autowired
    private RateDAOInterface rateDAO;

    @Transactional
    public void parseMegafonRates() {
        String url = "https://volgograd.megafon.ru/tariffs/all/";

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Не получилось подключиться к сайту megafon!");
            throw new RuntimeException(e);
        }
        Elements tariffElements = doc.select(".tariffs-carousel-v4__card-wrapper");

        for (Element tariffElement : tariffElements) {
            String name = tariffElement.select(".tariffs-card-header-v4__title-link").text();
            String[] rateDetails = tariffElement.select(".tariffs-card-additional-params-v4__value").text().split(" ");
            String priceStr = tariffElement.select(".tariffs-card-buy-v4__price").text();

            int gb = Integer.parseInt(rateDetails[0]);
            String minutesStr = rateDetails[2];
            int minutes;
            int price = parsePriceMegafon(priceStr);

            if (minutesStr.equals("мин")){
                minutes = 111111;
            } else {
                minutes = Integer.parseInt(minutesStr);
            }

            RateEntity rate = new RateEntity("Мегафон", "https://volgograd.megafon.ru/tariffs/all/", name, price, 0, minutes, gb);
            rateDAO.create(rate);
        }
    }

    private int parsePriceMegafon(String priceStr){
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(priceStr);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        throw new IllegalArgumentException("Цена не найдена в строке: " + priceStr);
    }
}
