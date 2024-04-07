package com.vaccine.Utils;

import com.vaccine.Model.Entity.User;

public class Session {
    private static Session INSTANCE;
    private User user;

    private Session() {
    }

    public static Session getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Session();
        }
        return INSTANCE;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}