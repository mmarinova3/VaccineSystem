package com.vaccine.Service;

import com.vaccine.Model.DAO.PersonVaccineDAO;
import com.vaccine.Model.Entity.PersonVaccine;
import com.vaccine.Utils.Session;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class PersonVaccineService {
    private static PersonVaccineService INSTANCE = null;
    private final PersonVaccineDAO personVaccineDAO;

    private PersonVaccineService(EntityManager entityManager, Session session) {
        this.personVaccineDAO = new PersonVaccineDAO(entityManager);
    }

    public static PersonVaccineService getInstance(EntityManager entityManager, Session session) {
        if (INSTANCE == null) {
            INSTANCE = new PersonVaccineService(entityManager,session);
        }
        return INSTANCE;
    }

    public PersonVaccine getById(int Id) {
        return personVaccineDAO.get(Id);
    }

    public List<PersonVaccine> getAll() {
        return personVaccineDAO.getAll();
    }

    public void save(PersonVaccine personVaccine) {
        personVaccineDAO.save(personVaccine);
    }

    public void update(PersonVaccine personVaccine, String[] params) {
        personVaccineDAO.update(personVaccine, params);
    }

    public void delete(PersonVaccine personVaccine) {
        personVaccineDAO.delete(personVaccine);
    }

    public List<PersonVaccine> getByPersonId(int Id) {
        return personVaccineDAO.getVaccinePersonsList(Id);
    }
    public PersonVaccine getByPersonAndVaccineId(int personId,int vaccineId){return personVaccineDAO.getByPersonAndVaccineId(personId,vaccineId);}
}
