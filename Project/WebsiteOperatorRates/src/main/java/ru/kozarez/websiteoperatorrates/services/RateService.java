package ru.kozarez.websiteoperatorrates.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kozarez.websiteoperatorrates.dao.RateDAOInterface;
import ru.kozarez.websiteoperatorrates.entities.RateEntity;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class RateService {
    @Autowired
    private RateDAOInterface rateDAO;

    @Transactional
    public void parseMegafonRates() {
        String providerName = "Мегафон";
        String url = "https://volgograd.megafon.ru/tariffs/all/";

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Не получилось подключиться к сайту megafon!");
            throw new RuntimeException(e);
        }
        Elements tariffElements = doc.select(".tariffs-carousel-v4__card-wrapper");

        if (!tariffElements.isEmpty()){
            deleteOldRates(providerName);
        }

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

            RateEntity rate = new RateEntity(providerName, url, name, price, 0, minutes, gb);
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

    @Transactional
    public void parseTele2Rates() {
        String providerName = "Tele2";
        String url = "https://volgograd.tele2.ru/tariffs";

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Не получилось подключиться к сайту tele2!");
            throw new RuntimeException(e);
        }
        Elements tariffElements = doc.select(".tariff-card.tariff-card_inline");

        if (!tariffElements.isEmpty()){
            deleteOldRates(providerName);
        }

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
            RateEntity rate = new RateEntity(providerName, url, name, price, sms, minutes, gb);
            rateDAO.create(rate);
        }
    }

    @Transactional
    public void parseBeelineRates() {
        String providerName = "Билайн";
        String url = "https://volgogradskaya-obl.beeline.ru/customers/products/mobile/tariffs/";

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Не получилось подключиться к сайту beeline!");
            throw new RuntimeException(e);
        }
        Elements tariffElements = doc.select(".EGVSC");

        if (!tariffElements.isEmpty()){
            deleteOldRates(providerName);
        }

        for (Element tariffElement : tariffElements) {
            String name = tariffElement.select(".AIVQ3.MKzVa.U1PW9.RMq5k").text();
            String[] rateDetails = tariffElement.select(".AIVQ3.W9Onj.U1PW9").text().split(" ");
            String[] priceStr = tariffElement.select(".AIVQ3.W9Onj.b1Q9I").text().split(" ");

            if(rateDetails[0].isBlank()){
                continue;
            }
            int gb = Integer.parseInt(rateDetails[0]);
            int minutes = Integer.parseInt(rateDetails[1]);
            int price = Integer.parseInt(priceStr[0]);
            int sms = 0;
            if(rateDetails.length == 3){
                sms = Integer.parseInt(rateDetails[2]);
            }

            RateEntity rate = new RateEntity(providerName, url, name, price, sms, minutes, gb);
            rateDAO.create(rate);
        }
    }


    @Transactional
    public void parseRostelekomRates() {
        String providerName = "Ростелеком";
        String url = "https://volgograd.rt.ru/mobile/mobile_tariff";
        WebDriver driver = new ChromeDriver();

        driver.get(url);
        driver.manage().window().fullscreen();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        List<WebElement> tariffElements = driver.findElements(By.cssSelector(".rt-tariff"));

        if (!tariffElements.isEmpty()){
            deleteOldRates(providerName);
        }

        int count = 0;
        for (WebElement tariffCard : tariffElements) {
            if (count == 3) {
                break;
            }

            String name = tariffCard.findElement(By.cssSelector(".card-title.js-dyn-hcell.card-title__link")).getText();
            List<WebElement> rateDetails = tariffCard.findElements(By.cssSelector(".d-inline-block"));
            int price = Integer.parseInt(tariffCard.findElement(By.cssSelector(".rt-price-v2__value.d-inline-block.rt-font-bold.font-h1")).getText().replaceAll("[^0-9]", ""));

            int gb = Integer.parseInt(rateDetails.get(0).getText().replaceAll("[^0-9]", ""));
            int minutes = Integer.parseInt(rateDetails.get(1).getText().replaceAll("[^0-9]", ""));
            int sms = Integer.parseInt(rateDetails.get(2).getText().replaceAll("[^0-9]", ""));

            count++;

            RateEntity rate = new RateEntity(providerName, url, name, price, sms, minutes, gb);
            rateDAO.create(rate);
        }

        driver.quit();
    }

    @Transactional
    public void parseTinkoffRates() {
        String providerName = "Тинькофф Мобайл";
        String url = "https://www.tinkoff.ru/mobile-operator/tariffs/";

        WebDriver driver = new ChromeDriver();

        driver.get(url);
        driver.manage().window().fullscreen();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        List<WebElement> tariffElements = driver.findElements(By.cssSelector(".abZbQCh9f"));

        if (!tariffElements.isEmpty()){
            deleteOldRates(providerName);
        }

        for (WebElement tariffCard : tariffElements) {
            String name = tariffCard.findElement(By.cssSelector(".aby1hK47R")).getText();
            List<WebElement> rateDetails = tariffCard.findElements(By.cssSelector(".fbfZ--oLmy"));
            int price = Integer.parseInt(tariffCard.findElement(By.cssSelector(".hbhmovsH3")).getText().replaceAll("[^0-9]", ""));

            int gb = Integer.parseInt(rateDetails.get(1).getText().replaceAll("[^0-9]", ""));
            int minutes = Integer.parseInt(rateDetails.get(0).getText().replaceAll("[^0-9]", ""));
            int sms = 0;

            RateEntity rate = new RateEntity(providerName, url, name, price, sms, minutes, gb);
            rateDAO.create(rate);
        }

        driver.quit();
    }

    private void deleteOldRates(String providerName){
        rateDAO.deleteByProviderName(providerName);
    }

    @Transactional
    public List<RateEntity> getRates() {
        return rateDAO.getAll();
    }

    @Transactional
    public List<RateEntity> getFilteredRates(Integer priceFrom, Integer priceTo, Integer gbFrom, Integer gbTo, Integer minutesFrom, Integer minutesTo, Integer messagesFrom, Integer messagesTo) {
        return rateDAO.getFilteredRates(priceFrom, priceTo, gbFrom, gbTo, minutesFrom, minutesTo, messagesFrom, messagesTo);
    }
}
