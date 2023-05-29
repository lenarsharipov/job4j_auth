package ru.job4j.auth.sevice;

import ru.job4j.auth.domain.Person;
import ru.job4j.auth.dto.PersonDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<Person> findById(int id);

    List<Person> findAll();

    Person save(PersonDTO personDTO);

    boolean update(Person person);

    boolean deleteById(int id);

    Optional<Person> findByLogin(String login);

}
