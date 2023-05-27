package ru.job4j.auth.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.sevice.PersonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
@Slf4j
public class PersonController {
    private final PersonService persons;

    @GetMapping("/")
    public List<Person> findAll() {
        return this.persons.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.persons.findById(id);
        return new ResponseEntity<>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return new ResponseEntity<>(
                this.persons.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<HttpStatus> update(@RequestBody Person person) {
        ResponseEntity<HttpStatus> response = ResponseEntity.ok(HttpStatus.OK);
        try {
            this.persons.save(person);
        } catch (Exception e) {
            response = ResponseEntity.of(
                    Optional.of(HttpStatus.NOT_FOUND)
            );
            log.error("UNABLE TO UPDATE PERSON", e);
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id) {
        ResponseEntity<HttpStatus> response = ResponseEntity.ok(HttpStatus.OK);
        Person person = new Person();
        person.setId(id);
        try {
            this.persons.delete(person);
        } catch (Exception e) {
            response = ResponseEntity.of(
                    Optional.of(HttpStatus.BAD_REQUEST)
            );
            log.error("UNABLE TO DELETE PERSON", e);
        }
        return response;
    }
}
