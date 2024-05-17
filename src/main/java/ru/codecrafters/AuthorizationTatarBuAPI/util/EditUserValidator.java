package ru.codecrafters.AuthorizationTatarBuAPI.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.codecrafters.AuthorizationTatarBuAPI.services.UsersService;

@Component
@RequiredArgsConstructor
public class EditUserValidator implements Validator {

    private final UsersService usersService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {//TODO
//        User updatedUser = (User)target;
//        Optional<User> userWithSameLogin = usersService.findOne(updatedUser.getLogin());
//        String loginOfCurrentUser = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
//
//        if(userWithSameLogin.isPresent() && !Objects.equals(userWithSameLogin.get().getLogin(), loginOfCurrentUser)){
//            errors.rejectValue("login", "", "This login is already occupied");
//        }
    }
}
