package com.vaccine.Model.DAO;

import com.vaccine.Model.Entity.User;
import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class UserDAO implements DAO<User> {
    private final static Logger log = LogManager.getLogger(User.class);
    private final EntityManager entityManager;

    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User get(int id) {
        try {
            return entityManager.find(User.class, id);
        } catch (Exception e) {
            log.error("User get error: " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<User> getAll() {
        try {
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        } catch (Exception e) {
            log.error("User getAll error: " + e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public void save(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("User save error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void update(User user, String[] params) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(user);
            entityManager.flush();
            transaction.commit();
        } catch (PersistenceException e) {
            throw e;
        } catch (Throwable e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("User update error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void delete(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                log.error("User delete error: " + e.getMessage(), e);
            }
        }
    }

    public User checkLogin(String enteredUsername, String enteredPassword) {
        try {
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password");
            query.setParameter("username", enteredUsername);
            query.setParameter("password", enteredPassword);
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            log.error("User login validation error: " + e.getMessage(), e);
            return null;
        } catch (Exception e) {
            log.error("Unexpected error during user login validation: " + e.getMessage(), e);
            return null;
        }
    }
    public boolean checkEmailExists(String email) {
        try {
            Query query = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email");
            query.setParameter("email", email);
            Long count = (Long) query.getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            return false;
        } catch (Exception e) {
            log.error("Error checking email existence: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean checkUsernameExists(String username) {
        try {
            Query query = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username");
            query.setParameter("username", username);
            Long count = (Long) query.getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            return false;
        } catch (Exception e) {
            log.error("Error checking username existence: " + e.getMessage(), e);
            return false;
        }
    }

    public Integer findIdByUsername(String username) {
        try {
            Query query = entityManager.createQuery("SELECT id FROM User WHERE username = :username");
            query.setParameter("username", username);
            return (Integer) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.error("Error finding id by username: " + e.getMessage(), e);
            return null;
        }
    }



}

