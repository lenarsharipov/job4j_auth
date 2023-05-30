package ru.job4j.auth.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank(message = "Login should not be empty")
    @NotNull(message = "Login should not be null")
    @Size(min = 3, message = "Login length should be min 3 characters length")
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @NotBlank(message = "Password should not be empty")
    @NotNull(message = "Password should not be null")
    @Size(min = 6, message = "Password should be at least 6 characters length")
    @Column(name = "password", nullable = false)
    private String password;
}
