package ru.job4j.auth.sevice;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.auth.domain.User;
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
        Optional<User> user = users.findByLogin(login);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(login);
        }
        return new org.springframework.security.core.userdetails.User(user.get().getLogin(),
                user.get().getPassword(), emptyList());
    }

    @Override
    public List<User> findAll() {
        return users.findAll();
    }

    @Override
    public Optional<User> findById(int id) {
        return users.findById(id);
    }

    @Override
    public User save(User user) {
        return users.save(user);
    }

    @Override
    public boolean update(User user) {
        var result = false;
        if (users.findById(user.getId()).isPresent()) {
            result = true;
            users.save(user);
        }
        return result;
    }

    @Override
    public boolean delete(int id) {
        var result = false;
        if (users.findById(id).isPresent()) {
            result = users.deleteById(id);
        }
        return result;
    }

}
