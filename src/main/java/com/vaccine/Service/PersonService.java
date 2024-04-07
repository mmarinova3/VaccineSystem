package com.vaccine.Service;

import com.vaccine.Model.DAO.PersonDAO;
import com.vaccine.Model.Entity.Person;
import com.vaccine.Utils.Session;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonService {

    private static PersonService INSTANCE = null;
    private final PersonDAO personDAO;

    private PersonService(EntityManager entityManager, Session session) {
        this.personDAO = new PersonDAO(entityManager);
    }

    public static PersonService getInstance(EntityManager entityManager, Session session) {
        if (INSTANCE == null) {
            INSTANCE = new PersonService(entityManager, session);
        }
        return INSTANCE;
    }


    public Optional<Person> getById(int id) {
        return personDAO.get(id);
    }

    public List<Person> getAll() {
        return personDAO.getAll();
    }

    public void save(Person person) throws PersistenceException {
        personDAO.save(person);
    }

    public void update(Person person) throws PersistenceException {
        personDAO.update(person, null); // You can pass parameters if needed
    }

    public void delete(Person person) throws PersistenceException {
        personDAO.delete(person);
    }
}
