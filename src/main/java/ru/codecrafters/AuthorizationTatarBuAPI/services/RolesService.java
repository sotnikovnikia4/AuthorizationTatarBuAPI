package ru.codecrafters.AuthorizationTatarBuAPI.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codecrafters.AuthorizationTatarBuAPI.exceptions.NotRegisteredException;
import ru.codecrafters.AuthorizationTatarBuAPI.models.Role;
import ru.codecrafters.AuthorizationTatarBuAPI.repotitories.RolesRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolesService {
    private final RolesRepository rolesRepository;

    public int getRoleId(String name){
        Optional<Role> role = rolesRepository.findByName(name);

        if(role.isEmpty())
            throw new NotRegisteredException("Такой роли не существует!");

        return role.get().getId();
    }
}
