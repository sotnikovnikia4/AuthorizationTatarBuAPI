package ru.codecrafters.AuthorizationTatarBuAPI.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {//TODO

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
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

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;
}
