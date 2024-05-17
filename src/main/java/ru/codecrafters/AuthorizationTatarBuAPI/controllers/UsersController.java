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
import ru.codecrafters.AuthorizationTatarBuAPI.services.UsersService;
import ru.codecrafters.AuthorizationTatarBuAPI.util.EditUserValidator;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UsersController {

    private final ModelMapper modelMapper;
    private final EditUserValidator editUserValidator;
    private final UsersService usersService;

    private final PasswordEncoder passwordEncoder;

    @PatchMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    public void edit(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){
        User newUserData = convertToUser(userDTO);
        editUserValidator.validate(newUserData, bindingResult);

        if(bindingResult.hasErrors()){
            throw new UserDataNotChangedException("User data is not changed, invalid data");
        }

        newUserData.setPassword(passwordEncoder.encode(newUserData.getPassword()));

        User currentUser = ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        usersService.update(newUserData, currentUser.getId());
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(){
        User currentUser = ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        usersService.delete(currentUser);
    }

    private User convertToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }
}
