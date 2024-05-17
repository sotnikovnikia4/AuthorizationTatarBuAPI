package ru.codecrafters.AuthorizationTatarBuAPI.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.codecrafters.AuthorizationTatarBuAPI.models.User;
import ru.codecrafters.AuthorizationTatarBuAPI.security.UserDetailsImpl;
import ru.codecrafters.AuthorizationTatarBuAPI.services.UsersService;
import ru.codecrafters.AuthorizationTatarBuAPI.util.EditUserValidator;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(){
        User currentUser = ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        usersService.delete(currentUser);
    }
}
