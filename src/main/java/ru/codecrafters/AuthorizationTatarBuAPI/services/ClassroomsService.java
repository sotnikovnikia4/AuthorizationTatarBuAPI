package ru.codecrafters.AuthorizationTatarBuAPI.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.codecrafters.AuthorizationTatarBuAPI.models.Classroom;
import ru.codecrafters.AuthorizationTatarBuAPI.models.User;
import ru.codecrafters.AuthorizationTatarBuAPI.repotitories.ClassroomsRepository;
import ru.codecrafters.AuthorizationTatarBuAPI.security.UserDetailsImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassroomsService {
    private final ClassroomsRepository classroomsRepository;

    public Classroom createByName(String name){
        Classroom classroom = new Classroom();
        classroom.setName(name);
        classroom.setTeacher(((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());

        classroomsRepository.save(classroom);

        return classroom;
    }

    public List<Classroom> getClassroomsByTeacher(User teacher){
        return classroomsRepository.findAllByTeacher(teacher);
    }

    public Optional<Classroom> getByTeacherId(User teacher){

    }
}
