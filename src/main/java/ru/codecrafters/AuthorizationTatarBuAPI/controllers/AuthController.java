package ru.codecrafters.AuthorizationTatarBuAPI.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.codecrafters.AuthorizationTatarBuAPI.dto.AuthenticationDTO;
import ru.codecrafters.AuthorizationTatarBuAPI.dto.RegistrationDTO;
import ru.codecrafters.AuthorizationTatarBuAPI.exceptions.NotRegisteredException;
import ru.codecrafters.AuthorizationTatarBuAPI.models.User;
import ru.codecrafters.AuthorizationTatarBuAPI.security.AuthManagerImpl;
import ru.codecrafters.AuthorizationTatarBuAPI.security.JWTUtil;
import ru.codecrafters.AuthorizationTatarBuAPI.security.UserDetailsImpl;
import ru.codecrafters.AuthorizationTatarBuAPI.services.RegistrationService;
import ru.codecrafters.AuthorizationTatarBuAPI.util.ErrorMessageMaker;
import ru.codecrafters.AuthorizationTatarBuAPI.util.RegistrationValidator;

import java.util.Map;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationValidator registrationValidator;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;

    private final AuthManagerImpl authManager;

    private final ModelMapper modelMapper;

    @PostMapping("/registration")
    public ResponseEntity<Map<String,Object>> register(@RequestBody @Valid RegistrationDTO registrationDTO,
                                                       BindingResult bindingResult){

        User user = convertToUser(registrationDTO);
        registrationValidator.validate(user, bindingResult);

        if(bindingResult.hasErrors()){
            throw new NotRegisteredException(ErrorMessageMaker.formErrorMessage(bindingResult));
        }

        registrationService.register(user);

        String token = jwtUtil.generateToken(user);

        return new ResponseEntity<>(
                Map.of("token", token),
                HttpStatus.OK
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@Valid @RequestBody AuthenticationDTO authenticationDTO, BindingResult bindingResult){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.getLogin(),
                authenticationDTO.getPassword()
        );

        Authentication authentication = authManager.authenticate(authToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtUtil.generateToken(userDetails.getUser());

        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }

    private User convertToUser(RegistrationDTO registrationDTO){
        return modelMapper.map(registrationDTO, User.class);
    }
}
