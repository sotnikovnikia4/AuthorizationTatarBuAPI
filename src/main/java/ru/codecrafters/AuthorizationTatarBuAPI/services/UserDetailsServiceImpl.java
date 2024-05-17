package ru.codecrafters.AuthorizationTatarBuAPI.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.codecrafters.AuthorizationTatarBuAPI.models.User;
import ru.codecrafters.AuthorizationTatarBuAPI.repotitories.UsersRepository;
import ru.codecrafters.AuthorizationTatarBuAPI.security.UserDetailsImpl;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> foundUser = usersRepository.findByLogin(username);

        if(foundUser.isEmpty())
            throw new UsernameNotFoundException("Имя пользователя не найдено!");

        return new UserDetailsImpl(foundUser.get());
    }
}
