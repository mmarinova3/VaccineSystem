package com.vaccine.Service;

import com.vaccine.Model.DAO.VaccineDAO;
import com.vaccine.Model.Entity.Vaccine;
import jakarta.persistence.EntityManager;

import java.util.List;

public class VaccineService {

    private static VaccineService INSTANCE = null;
    private final VaccineDAO vaccineDAO;

    private VaccineService(EntityManager entityManager) {
        this.vaccineDAO = new VaccineDAO(entityManager);
    }

    public static VaccineService getInstance(EntityManager entityManager) {
        if (INSTANCE == null) {
            INSTANCE = new VaccineService(entityManager);
        }
        return INSTANCE;
    }

    public Vaccine getById(int Id) {
        return vaccineDAO.get(Id);
    }

    public List<Vaccine> getAll() {
        return vaccineDAO.getAll();
    }

    public void save(Vaccine vaccine) {
        vaccineDAO.save(vaccine);
    }

    public void update(Vaccine vaccine, String[] params) {
        vaccineDAO.update(vaccine, params);
    }

    public void delete(Vaccine vaccine) {
        vaccineDAO.delete(vaccine);
    }

    public List<Vaccine> getUnassignedVaccinesForPerson(int Id) {
        return vaccineDAO.getUnassignedVaccinesForPerson(Id);
    }

    public List<Vaccine> getAssignedVaccinesForPerson(int Id) {
        return vaccineDAO.getAssignedVaccinesForPerson(Id);
    }

}
