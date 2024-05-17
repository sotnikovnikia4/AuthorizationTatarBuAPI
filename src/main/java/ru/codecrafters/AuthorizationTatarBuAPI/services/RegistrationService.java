package ru.codecrafters.AuthorizationTatarBuAPI.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.codecrafters.AuthorizationTatarBuAPI.models.User;
import ru.codecrafters.AuthorizationTatarBuAPI.repotitories.UsersRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(User user){
        enrich(user);
        usersRepository.save(user);
    }

    private void enrich(User user){//TODO
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRegisteredAt(LocalDateTime.now());
    }
}
