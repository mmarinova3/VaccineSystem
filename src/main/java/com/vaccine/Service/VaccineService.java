package com.vaccine.Service;

import com.vaccine.Model.DAO.VaccineDAO;
import com.vaccine.Model.Entity.Vaccine;
import com.vaccine.Utils.Session;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class VaccineService {

    private static VaccineService INSTANCE = null;
    private final VaccineDAO vaccineDAO;

    private VaccineService(EntityManager entityManager, Session session) {
        this.vaccineDAO = new VaccineDAO(entityManager);
    }

    public static VaccineService getInstance(EntityManager entityManager, Session session) {
        if (INSTANCE == null) {
            INSTANCE = new VaccineService(entityManager,session);
        }
        return INSTANCE;
    }

    public Optional<Vaccine> getById(int Id) {
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

}
