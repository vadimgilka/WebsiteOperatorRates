package ru.kozarez.websiteoperatorrates.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ru.kozarez.websiteoperatorrates.entities.RateEntity;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RateDAOImplementation implements RateDAOInterface {
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

    @Override
    public List<RateEntity> getFilteredRates(Integer priceFrom, Integer priceTo, Integer gbFrom, Integer gbTo, Integer minutesFrom, Integer minutesTo, Integer messagesFrom, Integer messagesTo) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RateEntity> query = cb.createQuery(RateEntity.class);
        Root<RateEntity> root = query.from(RateEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (priceFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), priceFrom));
        }
        if (priceTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), priceTo));
        }
        if (gbFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("gigabytesOfInternet"), gbFrom));
        }
        if (gbTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("gigabytesOfInternet"), gbTo));
        }
        if (minutesFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("minutesOfCall"), minutesFrom));
        }
        if (minutesTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("minutesOfCall"), minutesTo));
        }
        if (messagesFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("messages"), messagesFrom));
        }
        if (messagesTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("messages"), messagesTo));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }

    public void deleteByProviderName(String providerName) {
        entityManager.createQuery("DELETE FROM RateEntity r WHERE r.providerName = :providerName")
                .setParameter("providerName", providerName)
                .executeUpdate();
    }
}
