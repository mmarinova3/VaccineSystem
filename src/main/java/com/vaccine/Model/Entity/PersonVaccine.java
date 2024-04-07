package com.vaccine.Model.Entity;

import jakarta.persistence.*;

@Entity

@Table(name = "PersonVaccine", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "fk_personId", columnList = "personId"),
        @Index(name = "fk_vaccineId", columnList = "vaccineId"),
        @Index(name = "note", columnList = "note"),
})
public class PersonVaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_personId", referencedColumnName = "personId")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "fk_vaccineId", referencedColumnName = "vaccineId")
    private Vaccine vaccine;

    @Column(name = "note")
    private String note;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}