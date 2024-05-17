package ru.codecrafters.AuthorizationTatarBuAPI.repotitories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codecrafters.AuthorizationTatarBuAPI.models.Role;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
