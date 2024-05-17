package ru.codecrafters.AuthorizationTatarBuAPI.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {//TODO

    private UUID id;

    @Column(name = "login")
    @NotBlank
    private String login;

    @Column(name = "password")
    @NotBlank
    private String password;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;
}

