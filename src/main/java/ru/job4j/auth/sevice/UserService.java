package ru.job4j.auth.sevice;

import ru.job4j.auth.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(int id);

    List<User> findAll();

    User save(User user);

    boolean update(User user);

    boolean delete(int id);

}
