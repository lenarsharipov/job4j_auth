package ru.job4j.auth.sevice;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

import static java.util.Collections.emptyList;

/**
 * UserDetailsService - будет загружать в SecurityHolder детали авторизованного пользователя.
 */
@ThreadSafe
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private PersonRepository users;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Person> user = users.findByLogin(login);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(login);
        }
        return new User(user.get().getLogin(), user.get().getPassword(), emptyList());
    }
}
