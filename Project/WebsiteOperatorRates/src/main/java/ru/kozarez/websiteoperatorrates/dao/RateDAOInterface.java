package ru.kozarez.websiteoperatorrates.dao;

import ru.kozarez.websiteoperatorrates.entities.RateEntity;

import java.util.List;

public interface RateDAOInterface {
    RateEntity getById(Long id);

    List<RateEntity> getAll();

    void create(RateEntity rate);

    void update(RateEntity rate);

    void delete(Long id);
}
