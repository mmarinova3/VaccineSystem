package com.vaccine.Service;

import com.vaccine.Model.DAO.UserDAO;
import com.vaccine.Model.Entity.User;
import com.vaccine.Utils.Session;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class UserService {
    private static UserService INSTANCE = null;
    private final UserDAO userDao;
    private final Session session;

    private UserService(EntityManager entityManager, Session session) {
        this.userDao = new UserDAO(entityManager);
        this.session = session;
    }

    public static UserService getInstance(EntityManager entityManager, Session session) {
        if (INSTANCE == null) {
            INSTANCE = new UserService(entityManager, session);
        }
        return INSTANCE;
    }


    public Optional<User> getById(int Id) {
        return userDao.get(Id);
    }

    public List<User> getAll() {
        return userDao.getAll();
    }

    public void save(User user) {
        userDao.save(user);
    }

    public void update(User user, String[] params) {
        userDao.update(user, params);
    }

    public void delete(User user) {
        userDao.delete(user);
    }


}

