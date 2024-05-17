package ru.codecrafters.AuthorizationTatarBuAPI.repotitories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codecrafters.AuthorizationTatarBuAPI.models.Classroom;

import java.util.UUID;

@Repository
public interface ClassroomsRepository extends JpaRepository<Classroom, UUID> {
}
