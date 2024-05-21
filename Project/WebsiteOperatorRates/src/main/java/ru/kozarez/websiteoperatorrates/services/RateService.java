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

            RateEntity rate = new RateEntity("Мегафон", url, name, price, 0, minutes, gb);
            rateDAO.create(rate);
        }
    }

    @Transactional
    public void parseTele2Rates() {
        String url = "https://volgograd.tele2.ru/tariffs";

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Не получилось подключиться к сайту tele2!");
            throw new RuntimeException(e);
        }
        Elements tariffElements = doc.select(".tariff-card.tariff-card_inline");

        for (Element tariffElement : tariffElements){
            String name = tariffElement.select(".tariff-card__title-link").text();
            if (name.contains("Интернет") || name.isBlank()){
                continue;
            }
            String[] rateDetails = tariffElement.select(".tariff-card-parameter__value").text().split(" ");
            String priceStr = tariffElement.select(".tariff-abonent-fee__current-price-value").text();
            if (priceStr.isBlank()){
                continue;
            }
            int price = Integer.parseInt(priceStr);

            int minutes = Integer.parseInt(rateDetails[0]);
            int gb = Integer.parseInt(rateDetails[1]);
            int sms = 0;
            if (rateDetails.length == 4){
                sms = Integer.parseInt(rateDetails[3]);
            }
            if (rateDetails.length == 3 && !rateDetails[2].contains("+")) {
                sms = Integer.parseInt(rateDetails[2]);
            }
            RateEntity rate = new RateEntity("Tele2", url, name, price, sms, minutes, gb);
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
