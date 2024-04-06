package com.vaccine.Model.DAO;

import com.vaccine.Model.Entity.Person;
import com.vaccine.Model.Entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class PersonDAO implements DAO<Person>{
    private final static Logger log = LogManager.getLogger(User.class);
    private final EntityManager entityManager;

    public PersonDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public Optional<Person> get(int id) {
        try {
            return Optional.ofNullable(entityManager.find(Person.class, id));
        } catch (Exception e) {
            log.error("Person get error: " + e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<Person> getAll() {
        try {
            TypedQuery<Person> query = entityManager.createQuery("SELECT u FROM Person u", Person.class);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Person getAll error: " + e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public void save(Person person) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(person);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Person save error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void update(Person person, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(person);
            entityManager.flush();
            transaction.commit();
        } catch (PersistenceException e) {
            throw e;
        } catch (Throwable e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Person update error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void delete(Person person) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(person);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Person delete error: " + e.getMessage(), e);
            }
        }
    }
}
