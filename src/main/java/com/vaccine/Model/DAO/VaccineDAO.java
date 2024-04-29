package com.vaccine.Model.DAO;

import com.vaccine.Model.Entity.User;
import com.vaccine.Model.Entity.Vaccine;
import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class VaccineDAO implements DAO<Vaccine> {

    private final static Logger log = LogManager.getLogger(User.class);
    private final EntityManager entityManager;

    public VaccineDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public Vaccine get(int id) {
        try {
            return entityManager.find(Vaccine.class, id);
        } catch (Exception e) {
            log.error("Vaccine get error: " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Vaccine> getAll() {
        try {
            TypedQuery<Vaccine> query = entityManager.createQuery("SELECT u FROM Vaccine u", Vaccine.class);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Vaccine getAll error: " + e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public void save(Vaccine vaccine) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(vaccine);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Vaccine save error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void update(Vaccine vaccine, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(vaccine);
            entityManager.flush();
            transaction.commit();
        } catch (PersistenceException e) {
            throw e;
        } catch (Throwable e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Vaccine update error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void delete(Vaccine vaccine) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(vaccine);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("Vaccine delete error: " + e.getMessage(), e);
            }
        }
    }

    public List<Vaccine> getUnassignedVaccinesForPerson(int personId) {
        try {
            TypedQuery<Vaccine> query = entityManager.createQuery(
                    "SELECT v FROM Vaccine v " +
                            "LEFT JOIN PersonVaccine pv " +
                            "ON v.id = pv.vaccine.id AND pv.person.id = :personId " +
                            "WHERE pv.vaccine IS NULL", Vaccine.class);
            query.setParameter("personId", personId);
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Error finding unassigned vaccines for person: " + e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Vaccine> getAssignedVaccinesForPerson(int personId) {
        try {
            TypedQuery<Vaccine> query = entityManager.createQuery(
                    "SELECT v FROM Vaccine v " +
                            "LEFT JOIN PersonVaccine pv " +
                            "ON v.id = pv.vaccine.id AND pv.person.id = :personId " +
                            "WHERE pv.vaccine IS NOT NULL", Vaccine.class);
            query.setParameter("personId", personId);
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Error finding assigned vaccines for person: " + e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
