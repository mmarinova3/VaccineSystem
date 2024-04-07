package com.vaccine.Model.DAO;

import com.vaccine.Model.Entity.Person;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class PersonDAO implements DAO<Person> {

    private final EntityManager entityManager;
    private static final Logger log = LogManager.getLogger(PersonDAO.class);

    @Autowired
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
        try {
            entityManager.persist(person);
        } catch (Exception e) {
            log.error("Person save error: " + e.getMessage(), e);
            throw new PersistenceException(e);
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
}
