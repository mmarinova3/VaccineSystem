package com.vaccine.Model.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Table(name = "Person", indexes = {
        @Index(name = "personId", columnList = "personId", unique = true),
        @Index(name = "fk_userId", columnList = "userId"),
        @Index(name = "name", columnList = "name"),
        @Index(name = "dateOfBirth", columnList = "dateOfBirth"),
        @Index(name = "relationWithUser", columnList = "relationWithUser")
})

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "personId", nullable = false)
    private int id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "dateOfBirth", nullable = false)
    private java.sql.Date dateOfBirth;

    @Column(name = "relationWithUser")
    private String relationWithUser;

    public Person() {
    }

    public Person(int id,User user, String name, Date dateOfBirth) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.dateOfBirth = (java.sql.Date) dateOfBirth;
        this.relationWithUser=null;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return  dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = (java.sql.Date) dateOfBirth;
    }

    public String getRelationWithUser() {
        return relationWithUser;
    }

    public void setRelationWithUser(String relationWithUser) {
        this.relationWithUser = relationWithUser;
    }
}
