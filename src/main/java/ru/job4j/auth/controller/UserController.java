package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.domain.User;
import ru.job4j.auth.sevice.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private BCryptPasswordEncoder encoder;
    private final UserService users;
    private final ObjectMapper objectMapper;

    @GetMapping("/all")
    public List<User> findAll() {
        return this.users.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable int id) {
        var user = this.users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User Not Found By id: " + id
                ));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<User> create(@RequestBody User user) {
        if (user == null || user.getLogin() == null || user.getPassword() == null) {
            throw new NullPointerException("Login And Password Should Not Be Empty");
        }
        if (this.users.findByLogin(user.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Login " + user.getLogin() + " is occupied.");
        }
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password Should be at least 6 characters length");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return new ResponseEntity<>(
                this.users.save(user),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<String> update(@RequestBody User user) {
        if (user == null || user.getLogin() == null || user.getPassword() == null) {
            throw new NullPointerException("Login And Password Should Not Be Empty");
        }
        if (users.findById(user.getId()).isEmpty()) {
            throw new IllegalArgumentException("User Not Found By id: " + user.getId());
        }
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password Should be at least 6 characters length");
        }
        ResponseEntity<String> response =
                new ResponseEntity<>("Person updated", HttpStatus.OK);
        user.setPassword(encoder.encode(user.getPassword()));
        if (!users.update(user)) {
            response = new ResponseEntity<>(
                    "Unable to update person",
                    HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        if (users.findById(id).isEmpty()) {
            throw new IllegalArgumentException("User Not Found By id: " + id);
        }
        ResponseEntity<String> response =
                new ResponseEntity<>(
                        "Person deleted",
                        HttpStatus.OK);
        if (!users.deleteById(id)) {
            response = new ResponseEntity<>(
                    "Unable to delete Person with id: " + id,
                    HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody User user) {
        if (user == null || user.getLogin() == null || user.getPassword() == null) {
            throw new NullPointerException("Login And Password Should Not Be Empty");
        }
        if (this.users.findByLogin(user.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Login " + user.getLogin() + " is occupied.");
        }
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password Should be at least 6 characters length");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        users.save(user);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void handleException(Exception exception,
                                HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", exception.getMessage());
            put("details", exception.getClass());
        }}));
        log.error(exception.getMessage());
    }
}
