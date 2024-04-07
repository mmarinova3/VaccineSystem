package com.vaccine.Model.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "Vaccine", indexes = {
        @Index(name = "vaccineId", columnList = "vaccineId", unique = true),
        @Index(name = "vaccineName", columnList = "vaccineName"),
        @Index(name = "info", columnList = "info"),
        @Index(name = "vaccineAge", columnList = "vaccineAge"),
        @Index(name = "period", columnList = "period"),
        @Index(name = "doseNumber", columnList = "doseNumber"),
        @Index(name = "isMade", columnList = "isMade")
})

public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "vaccineId", nullable = false)
    private Integer id;

    @Column(name = "vaccineName", length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String vaccineName;

    @Lob
    @Column(name = "info")
    @JdbcTypeCode(SqlTypes.LONG32VARCHAR)
    private String info;

    @Column(name = "vaccineAge")
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer vaccineAge;

    @Column(name = "period")
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer period;

    @Column(name = "doseNumber")
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer doseNumber;

    @Column(name = "isMade")
    @JdbcTypeCode(SqlTypes.BOOLEAN)
    private boolean isMade;

    public Integer getDoseNumber() {
        return doseNumber;
    }

    public void setDoseNumber(Integer doseNumber) {
        this.doseNumber = doseNumber;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getVaccineAge() {
        return vaccineAge;
    }

    public void setVaccineAge(Integer vaccineAge) {
        this.vaccineAge = vaccineAge;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}