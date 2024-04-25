package com.vaccine.Model.DAO;

import com.vaccine.Model.Entity.PersonVaccine;
import com.vaccine.Model.Entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class PersonVaccineDAO implements DAO<PersonVaccine> {
    private final static Logger log = LogManager.getLogger(User.class);
    private final EntityManager entityManager;

    public PersonVaccineDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public PersonVaccine get(int id) {
        try {
            return entityManager.find(PersonVaccine.class, id);
        } catch (Exception e) {
            log.error("PersonVaccine get error: " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<PersonVaccine> getAll() {
        try {
            TypedQuery<PersonVaccine> query = entityManager.createQuery("SELECT u PersonVaccine User u", PersonVaccine.class);
            return query.getResultList();
        } catch (Exception e) {
            log.error("PersonVaccine getAll error: " + e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public void save(PersonVaccine personVaccine) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(personVaccine);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("PersonVaccine save error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void update(PersonVaccine personVaccine, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(personVaccine);
            entityManager.flush();
            transaction.commit();
        } catch (PersistenceException e) {
            throw e;
        } catch (Throwable e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("PersonVaccine update error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void delete(PersonVaccine personVaccine) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(personVaccine);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("PersonVaccine delete error: " + e.getMessage(), e);
            }
        }
    }
}
