package com.vaccine.vaccinesystem.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.Date;

@Table(name = "person", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "fkUserId", columnList = "userId", unique = true),
        @Index(name = "name", columnList = "name"),
        @Index(name = "dateOfBirth", columnList = "dateOfBirth"),
        @Index(name = "relationWithUser", columnList = "relationWithUser")
})

@Entity
public class Person implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private int id;

    @ManyToOne
    @Column(name = "userId", nullable = false, length = 50)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private int userId;

    @Column(name = "name", nullable = false, length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String name;

    @Column(name = "dateOfBirth", nullable = false)
    @JdbcTypeCode(SqlTypes.DATE)
    private Date dateOfBirth;

    @Column(name = "relationWithUser", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String relationWithUser;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRelationWithUser() {
        return relationWithUser;
    }

    public void setRelationWithUser(String relationWithUser) {
        this.relationWithUser = relationWithUser;
    }
}
