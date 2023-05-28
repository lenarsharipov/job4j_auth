package ru.job4j.auth.sevice;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    @Override
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @Override
    public boolean update(Person person) {
        var result = false;
        if (personRepository.findById(person.getId()).isPresent()) {
            result = true;
            personRepository.save(person);
        }
        return result;
    }

    @Override
    public boolean delete(int id) {
        var result = false;
        if (personRepository.findById(id).isPresent()) {
            result = personRepository.deleteById(id);
        }
        return result;
    }

}
