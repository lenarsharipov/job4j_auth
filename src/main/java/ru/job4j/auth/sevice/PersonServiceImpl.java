package ru.job4j.auth.sevice;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {
    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceImpl.class.getName());
    private final PersonRepository personRepository;

    @Override
    public List<Person> findAll() {
        List<Person> result = Collections.emptyList();
        try {
            result = personRepository.findAll();
        } catch (Exception exception) {
            LOG.error("UNABLE TO LIST PERSONS", exception);
        }
        return result;
    }

    @Override
    public Optional<Person> findById(int id) {
        Optional<Person> result = Optional.empty();
        try {
            result = personRepository.findById(id);
        } catch (Exception exception) {
            LOG.error("UNABLE TO FIND PERSON BY ID", exception);
        }
        return result;
    }

    @Override
    public Person save(Person person) {
        var result = new Person();
        try {
            result = personRepository.save(person);
        } catch (Exception exception) {
            LOG.error("UNABLE TO SAVE PERSON", exception);
        }
        return result;
    }

    @Override
    public boolean update(Person person) {
        var result = true;
        try {
            personRepository.save(person);
        } catch (Exception exception) {
            result = false;
            LOG.error("UNABLE TO UPDATE PERSON", exception);
        }
        return result;
    }

    @Override
    public boolean delete(Person person) {
        var result = true;
        try {
            personRepository.delete(person);
        } catch (Exception exception) {
            result = false;
            LOG.error("UNABLE TO DELETE PERSON", exception);
        }
        return result;
    }
}
