package ru.kozarez.websiteoperatorrates.dao;

import ru.kozarez.websiteoperatorrates.entities.RateEntity;

import java.util.List;

public interface RateDAOInterface {
    RateEntity getById(Long id);

    List<RateEntity> getAll();

    void create(RateEntity rate);

    void update(RateEntity rate);

    void delete(Long id);

    List<RateEntity> getFilteredRates(Integer priceFrom, Integer priceTo, Integer gbFrom, Integer gbTo, Integer minutesFrom, Integer minutesTo, Integer messagesFrom, Integer messagesTo);
}
