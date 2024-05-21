package ru.kozarez.websiteoperatorrates.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.kozarez.websiteoperatorrates.entities.RateEntity;

import java.util.List;

@Repository
public class RateDAOImplementation implements RateDAOInterface{
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public RateEntity getById(Long id) {
        RateEntity rate = entityManager.find(RateEntity.class, id);
        entityManager.detach(rate);
        return rate;
    }

    @Override
    public List<RateEntity> getAll() {
        return entityManager.createQuery("from RateEntity", RateEntity.class).getResultList();
    }

    @Override
    public void create(RateEntity rate) {
        entityManager.persist(rate);
    }

    @Override
    public void update(RateEntity rate) {
        entityManager.merge(rate);
    }

    @Override
    public void delete(Long id) {
        RateEntity rate = entityManager.find(RateEntity.class, id);
        if (rate != null) {
            entityManager.remove(rate);
        }
    }
}
