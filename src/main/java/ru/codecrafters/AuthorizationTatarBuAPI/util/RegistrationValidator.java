package ru.codecrafters.AuthorizationTatarBuAPI.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.codecrafters.AuthorizationTatarBuAPI.models.User;
import ru.codecrafters.AuthorizationTatarBuAPI.services.RegistrationService;
import ru.codecrafters.AuthorizationTatarBuAPI.services.UsersService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RegistrationValidator implements Validator {
    private final UsersService usersService;
    private final RegistrationService registrationService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User userToCheck = (User)target;
        Optional<User> userWithSameLogin = usersService.findOne(userToCheck.getLogin());

        if(userWithSameLogin.isPresent()){
            errors.rejectValue("login", "", "Пользователь с таким логином уже существует!");
        }
    }
}
