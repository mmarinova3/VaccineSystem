package com.vaccine.Model.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "Vaccine", indexes = {
        @Index(name = "vaccineId", columnList = "vaccineId", unique = true),
        @Index(name = "Name", columnList = "Name"),
        @Index(name = "AgeOfUse", columnList = "AgeOfUse"),
        @Index(name = "Info", columnList = "Info"),
        @Index(name = "ApplicationMethod", columnList = "ApplicationMethod"),
        @Index(name = "isMandatory", columnList = "isMandatory"),
        @Index(name = "isOneTime", columnList = "isOneTime"),
        @Index(name = "effectivenessPeriod", columnList = "effectivenessPeriod")
})

public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "vaccineId", nullable = false)
    private Integer id;

    @Column(name = "Name", length = 50)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String vaccineName;

    @Column(name = "AgeOfUse")
    @JdbcTypeCode(SqlTypes.INTEGER)
    private int ageOfUse;
    @Lob
    @Column(name = "Info")
    @JdbcTypeCode(SqlTypes.LONG32VARCHAR)
    private String info;

    @Column(name = "ApplicationMethod")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String applicationMethod;

    @Column(name = "isMandatory")
    @JdbcTypeCode(SqlTypes.BOOLEAN)
    private boolean isMandatory;
    @Column(name = "isOneTime")
    @JdbcTypeCode(SqlTypes.BOOLEAN)
    private boolean isOneTime;

    @Column(name = "effectivenessPeriod")
    @JdbcTypeCode(SqlTypes.INTEGER)
    private int effectivenessPeriod;

    public Vaccine() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public int getAgeOfUse() {
        return ageOfUse;
    }

    public void setAgeOfUse(int ageOfUse) {
        this.ageOfUse = ageOfUse;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getApplicationMethod() {
        return applicationMethod;
    }

    public void setApplicationMethod(String applicationMethod) {
        this.applicationMethod = applicationMethod;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public boolean isOneTime() {
        return isOneTime;
    }

    public void setOneTime(boolean oneTime) {
        isOneTime = oneTime;
    }

    public int getEffectivenessPeriod() {
        return effectivenessPeriod;
    }

    public void setEffectivenessPeriod(int effectivenessPeriod) {
        this.effectivenessPeriod = effectivenessPeriod;
    }
}