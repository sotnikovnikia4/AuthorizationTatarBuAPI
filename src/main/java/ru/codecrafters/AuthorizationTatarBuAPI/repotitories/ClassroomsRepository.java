package ru.codecrafters.AuthorizationTatarBuAPI.repotitories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codecrafters.AuthorizationTatarBuAPI.models.Classroom;
import ru.codecrafters.AuthorizationTatarBuAPI.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClassroomsRepository extends JpaRepository<Classroom, UUID> {
    List<Classroom> findAllByTeacher(User teacher);

}
