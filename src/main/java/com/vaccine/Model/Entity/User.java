package com.vaccine.Model.Entity;

import jakarta.persistence.*;

import java.io.Serializable;


@Table(name = "User", indexes = {
        @Index(name = "userId", columnList = "userId", unique = true),
        @Index(name = "username", columnList = "username", unique = true),
        @Index(name = "password", columnList = "password"),
        @Index(name = "email", columnList = "email")
})

@Entity
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "userId", nullable = false)
    private Integer id;


    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "email", length = 50)
    private String email;


    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
