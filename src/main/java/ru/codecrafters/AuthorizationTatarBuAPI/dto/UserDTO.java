package ru.codecrafters.AuthorizationTatarBuAPI.dto;

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

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;

    @Column(name="avatar")
    private Integer avatar;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;
}
