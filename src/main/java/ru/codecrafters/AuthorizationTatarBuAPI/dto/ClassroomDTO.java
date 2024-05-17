package ru.codecrafters.AuthorizationTatarBuAPI.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.codecrafters.AuthorizationTatarBuAPI.models.User;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomDTO {
    private UUID id;

    private String name;

    private UserDTO head;

    private List<UserDTO> students;
}
