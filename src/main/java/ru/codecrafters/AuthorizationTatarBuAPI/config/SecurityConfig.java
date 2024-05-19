package ru.codecrafters.AuthorizationTatarBuAPI.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig{//TODO

    private final JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(
                                        "/auth/login",
                                        "/auth/registration",
                                        "/error",
                                "/classrooms/student/*/get-classrooms"
                                )
                                .permitAll()
                                .requestMatchers(
                                        "/classrooms/create",
                                        "/classrooms/get-all",
                                        "/classrooms/get-one",
                                        "/classrooms/delete",
                                        "/classrooms/edit",
                                        "/classrooms/add-student",
                                        "/classrooms/remove-student"
                                ).hasRole("TEACHER")
                                .requestMatchers(
                                    "/classrooms/ask-join-group"
                                ).hasRole("PUPIL")
                                .requestMatchers(
                                        "/users/**",
                                        "/classrooms/**"
                                ).authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(m ->
                        m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();


    }


    @Bean
    public PasswordEncoder passwordEncoder(){
         return new BCryptPasswordEncoder();
    }
}
