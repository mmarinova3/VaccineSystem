package com.vaccine.Model.DAO;

import com.vaccine.Model.Entity.Person;
import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PersonDAO implements DAO<Person> {

    private final static Logger log = LogManager.getLogger(Person.class);
    private final EntityManager entityManager;

    public PersonDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Person get(int id) {
        try {
            return entityManager.find(Person.class, id);
        } catch (Exception e) {
            log.error("Person get error: " + e.getMessage(), e);
            return null;
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
        try {
            entityManager.merge(person);
        } catch (Exception e) {
            log.error("Person update error: " + e.getMessage(), e);
            throw new PersistenceException(e);
        }
    }

    @Override
    public void delete(Person person) {
        try {
            entityManager.remove(person);
        } catch (Exception e) {
            log.error("Person delete error: " + e.getMessage(), e);
            throw new PersistenceException(e);
        }
    }

    public List<Person> getPersonsList(int userId) {
        try {
            Query query = entityManager.createQuery("SELECT p FROM Person p WHERE p.user.id = :userId");
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Error finding persons by user ID: " + e.getMessage(), e);
            return null;
        }
    }

}
