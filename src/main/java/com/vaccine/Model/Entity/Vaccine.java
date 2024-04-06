package com.vaccine.Model.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "Vaccine")
public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "vaccine_name", length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String vaccineName;

    @Lob
    @Column(name = "info")
    @JdbcTypeCode(SqlTypes.LONG32VARCHAR)
    private String info;

    @Column(name = "vaccine_age")
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer vaccineAge;

    @Column(name = "period")
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer period;

    @Column(name = "dose_number")
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer doseNumber;

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