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


    public User getById(int Id) {
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

    public User validateLogin(String enteredUsername, String enteredPassword) {
        User user = userDao.checkLogin(enteredUsername, enteredPassword);
        if (user != null) {
            session.setUser(user);
        }
        return user;
    }

    public boolean checkEmailExists(String email){
      return  userDao.checkEmailExists(email);
    }

    public boolean checkUsernameExists(String username){
        return  userDao.checkUsernameExists(username);
    }

    public int getIdByUsername (String username){
        return userDao.findIdByUsername(username);
    }


}

