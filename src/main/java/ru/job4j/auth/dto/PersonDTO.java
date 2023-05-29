package ru.job4j.auth.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PersonDTO {
    private String login;
    private String password;
}
