package ru.codecrafters.AuthorizationTatarBuAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {

    @JsonProperty("name")
    @NotBlank
    private String name;

    @JsonProperty("login")
    @NotBlank
    private String login;

    @JsonProperty("password")
    @NotBlank
    private String password;

    @JsonProperty("role")
    @NotNull
    private RoleDTO role;

    @JsonProperty("avatar")
    private Integer avatar;

    @NotBlank
    private String gender;

    @JsonProperty("birth_date")
    @NotNull
    private LocalDateTime birthDate;
}

