package ru.job4j.auth.sevice;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.dto.PersonDTO;
import ru.job4j.auth.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

/**
 * UserDetailsService - будет загружать в SecurityHolder детали авторизованного пользователя.
 */
@ThreadSafe
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserService {
    private final UserRepository users;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Person> user = users.findByLogin(login);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(login);
        }
        return new org.springframework.security.core.userdetails.User(user.get().getLogin(),
                user.get().getPassword(), emptyList());
    }

    @Override
    public List<Person> findAll() {
        return users.findAll();
    }

    @Override
    public Optional<Person> findById(int id) {
        return users.findById(id);
    }

    @Override
    public Person save(PersonDTO personDTO) {
        var person = new Person();
        person.setLogin(personDTO.getLogin());
        person.setPassword(personDTO.getPassword());
        return users.save(person);
    }

    @Override
    public boolean update(Person person) {
        var result = false;
        if (users.findById(person.getId()).isPresent()) {
            result = true;
            users.save(person);
        }
        return result;
    }

    @Override
    public boolean deleteById(int id) {
        var result = false;
        if (users.findById(id).isPresent()) {
            users.deleteById(id);
            result = true;
        }
        return result;
    }

    @Override
    public Optional<Person> findByLogin(String login) {
        return users.findByLogin(login);
    }

}
