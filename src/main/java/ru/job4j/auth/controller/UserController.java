package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.dto.PersonDTO;
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
    public ResponseEntity<List<Person>> findAll() {
        var body = this.users.findAll();
        return new ResponseEntity<>(
                body,
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var user = this.users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User Not Found By id: " + id
                ));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody PersonDTO personDTO) {
        if (personDTO == null || personDTO.getLogin() == null || personDTO.getPassword() == null) {
            throw new NullPointerException("Login And Password Should Not Be Empty");
        }
        if (this.users.findByLogin(personDTO.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Login " + personDTO.getLogin() + " is occupied.");
        }
        if (personDTO.getLogin().length() < 3) {
            throw new IllegalArgumentException("Login Should be at least 3 characters length");
        }
        if (personDTO.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password Should be at least 6 characters length");
        }
        personDTO.setPassword(encoder.encode(personDTO.getPassword()));
        return new ResponseEntity<>(
                this.users.save(personDTO),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/")
    public ResponseEntity<String> update(@RequestBody Person person) {
        if (person == null || person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Login And Password Should Not Be Empty");
        }
        if (users.findById(person.getId()).isEmpty()) {
            throw new IllegalArgumentException("User Not Found By id: " + person.getId());
        }
        if (person.getLogin().length() < 3) {
            throw new IllegalArgumentException("Login Should be at least 3 characters length");
        }
        if (person.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password Should be at least 6 characters length");
        }
        ResponseEntity<String> response =
                new ResponseEntity<>("Person updated", HttpStatus.OK);
        person.setPassword(encoder.encode(person.getPassword()));
        if (!users.update(person)) {
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
    public ResponseEntity<Person> signUp(@RequestBody PersonDTO personDTO) {
        if (personDTO == null || personDTO.getLogin() == null || personDTO.getPassword() == null) {
            throw new NullPointerException("Login And Password Should Not Be Empty");
        }
        if (this.users.findByLogin(personDTO.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Login " + personDTO.getLogin() + " is occupied.");
        }
        if (personDTO.getLogin().length() < 3) {
            throw new IllegalArgumentException("Login Should be at least 3 characters length");
        }
        if (personDTO.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password Should be at least 6 characters length");
        }
        personDTO.setPassword(encoder.encode(personDTO.getPassword()));
        var person = users.save(personDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(person.toString().length())
                .body(person);
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
