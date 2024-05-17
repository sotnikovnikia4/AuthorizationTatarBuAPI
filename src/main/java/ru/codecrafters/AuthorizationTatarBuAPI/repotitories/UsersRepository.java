package ru.codecrafters.AuthorizationTatarBuAPI.repotitories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codecrafters.AuthorizationTatarBuAPI.models.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {

    Optional<User> findByLogin(String login);
}
