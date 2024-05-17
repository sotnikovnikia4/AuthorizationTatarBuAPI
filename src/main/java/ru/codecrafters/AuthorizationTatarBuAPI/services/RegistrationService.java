package ru.codecrafters.AuthorizationTatarBuAPI.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.codecrafters.AuthorizationTatarBuAPI.models.User;
import ru.codecrafters.AuthorizationTatarBuAPI.repotitories.RolesRepository;
import ru.codecrafters.AuthorizationTatarBuAPI.repotitories.UsersRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UsersRepository usersRepository;
    private final RolesService rolesService;
    private final PasswordEncoder passwordEncoder;

    public void register(User user){
        enrich(user);
        usersRepository.save(user);
    }

    private void enrich(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRegisteredAt(LocalDateTime.now());
        user.setLastActivityAt(LocalDateTime.now());
        user.getRole().setId(rolesService.getRoleId(user.getRole().getName()));
    }
}
