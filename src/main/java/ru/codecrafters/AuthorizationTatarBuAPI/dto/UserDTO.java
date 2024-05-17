package ru.codecrafters.AuthorizationTatarBuAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.codecrafters.AuthorizationTatarBuAPI.models.Role;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private UUID id;

    private String name;

    private String login;

    private RoleDTO role;

    private Integer avatar;

    private String gender;

    @JsonProperty("birth_date")
    private LocalDateTime birthDate;

    @JsonProperty("registered_at")
    private LocalDateTime registeredAt;

    @JsonProperty("last_activity_at")
    private LocalDateTime lastActivityAt;
}
