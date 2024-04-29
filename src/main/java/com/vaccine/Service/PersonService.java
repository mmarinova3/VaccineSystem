package com.vaccine.Service;

import com.vaccine.Model.DAO.PersonDAO;
import com.vaccine.Model.Entity.Person;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PersonService {

    private static PersonService INSTANCE = null;
    private final PersonDAO personDAO;

    private PersonService(EntityManager entityManager) {
        this.personDAO = new PersonDAO(entityManager);
    }

    public static PersonService getInstance(EntityManager entityManager) {
        if (INSTANCE == null) {
            INSTANCE = new PersonService(entityManager);
        }
        return INSTANCE;
    }


    public Person getById(int id) {
        return personDAO.get(id);
    }

    public List<Person> getAll() {
        return personDAO.getAll();
    }

    public void save(Person person)  {
        personDAO.save(person);
    }

    public void update(Person person)  {
        personDAO.update(person, null);
    }

    public void delete(Person person)  {
        personDAO.delete(person);
    }

    public List<Person> getPersonsList(int userId)  {
        return personDAO.getPersonsList(userId);
    }

}
