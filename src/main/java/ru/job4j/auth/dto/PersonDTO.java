package ru.job4j.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PersonDTO {
    @NotBlank(message = "Login should not be empty")
    @NotNull(message = "Login should not be null")
    @Size(min = 3, message = "Login length should be min 3 characters length")
    private String login;

    @NotBlank(message = "Password should not be empty")
    @NotNull(message = "Password should not be null")
    @Size(min = 6, message = "Password should be at least 6 characters length")
    private String password;
}
