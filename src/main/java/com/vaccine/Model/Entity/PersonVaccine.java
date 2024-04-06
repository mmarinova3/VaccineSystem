package com.vaccine.Model.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PersonVaccine")
public class PersonVaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToMany
    @Column(name = "personId", nullable = false)
    private Integer personId;

    @OneToMany
    @Column(name = "vaccineId", nullable = false)
    private Integer vaccineID;

    @Column(name = "note")
    private String note;

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Integer getVaccineID() {
        return vaccineID;
    }

    public void setVaccineID(Integer vaccineID) {
        this.vaccineID = vaccineID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}