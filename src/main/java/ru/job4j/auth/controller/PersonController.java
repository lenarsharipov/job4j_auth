package ru.job4j.auth.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.sevice.PersonService;

import java.util.List;

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
    public ResponseEntity<String> update(@RequestBody Person person) {
        ResponseEntity<String> response =
                new ResponseEntity<>("Person updated", HttpStatus.OK);
        if (!persons.update(person)) {
            response = new ResponseEntity<>(
                    "Unable to update person",
                    HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        ResponseEntity<String> response =
                new ResponseEntity<>(
                        "Person deleted",
                        HttpStatus.OK);
        if (!persons.delete(id)) {
            response = new ResponseEntity<>(
                    "Unable to delete Person with id: " + id,
                    HttpStatus.NOT_FOUND);
        }
        return response;
    }
}
