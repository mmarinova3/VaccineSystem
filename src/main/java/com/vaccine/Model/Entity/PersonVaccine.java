package com.vaccine.Model.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Date;

@Entity

@Table(name = "PersonVaccine", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "fk_personId", columnList = "personId"),
        @Index(name = "fk_vaccineId", columnList = "vaccineId"),
        @Index(name = "isMade", columnList = "isMade"),
        @Index(name = "vaccinationDay", columnList = "vaccinationDay"),
        @Index(name = "note", columnList = "note")
})

public class PersonVaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_personId", referencedColumnName = "personId", nullable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "fk_vaccineId", referencedColumnName = "vaccineId", nullable = false)
    private Vaccine vaccine;

    @Column(name = "isMade", nullable = false)
    @JdbcTypeCode(SqlTypes.BOOLEAN)
    private boolean isMade;
    @Column(name = "vaccinationDate")
    private java.sql.Date vaccinationDate;
    @Column(name = "note")
    private String note;

    public PersonVaccine() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
    }

    public boolean isMade() {
        return isMade;
    }

    public void setMade(boolean made) {
        isMade = made;
    }

    public Date getVaccinationDate() {
        return vaccinationDate;
    }

    public void setVaccinationDate(Date vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}