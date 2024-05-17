package ru.codecrafters.AuthorizationTatarBuAPI.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.codecrafters.AuthorizationTatarBuAPI.models.Classroom;
import ru.codecrafters.AuthorizationTatarBuAPI.repotitories.ClassroomsRepository;
import ru.codecrafters.AuthorizationTatarBuAPI.security.UserDetailsImpl;

@Service
@RequiredArgsConstructor
public class ClassroomsService {
    private final ClassroomsRepository classroomsRepository;

    public Classroom createByName(String name){
        Classroom classroom = new Classroom();
        classroom.setName(name);
        classroom.setHead(((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());

        classroomsRepository.save(classroom);

        return classroom;
    }
}
