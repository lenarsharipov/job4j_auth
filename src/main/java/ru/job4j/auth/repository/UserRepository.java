package ru.job4j.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.auth.domain.Person;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<Person, Integer> {
    List<Person> findAll();

    Optional<Person> findByLogin(String login);

}
