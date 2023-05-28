package ru.job4j.auth.sevice;

import ru.job4j.auth.domain.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    Optional<Person> findById(int id);

    List<Person> findAll();

    Person save(Person person);

    boolean update(Person person);

    boolean delete(int id);

}
