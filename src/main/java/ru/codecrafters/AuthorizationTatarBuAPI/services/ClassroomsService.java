package ru.codecrafters.AuthorizationTatarBuAPI.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.codecrafters.AuthorizationTatarBuAPI.exceptions.ClassroomException;
import ru.codecrafters.AuthorizationTatarBuAPI.models.Classroom;
import ru.codecrafters.AuthorizationTatarBuAPI.models.User;
import ru.codecrafters.AuthorizationTatarBuAPI.repotitories.ClassroomsRepository;
import ru.codecrafters.AuthorizationTatarBuAPI.repotitories.UsersRepository;
import ru.codecrafters.AuthorizationTatarBuAPI.security.UserDetailsImpl;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassroomsService {
    private static final int MAX_COUNT_STUDENTS = 30;

    private final ClassroomsRepository classroomsRepository;
    private final UsersRepository usersRepository;
    private User systemBot;
    private final Random random;

    @PostConstruct
    public void configure(){
        systemBot = usersRepository.findByLogin("SYSTEM_BOT").orElse(null);
    }

    public Classroom createByName(String name){
        Classroom classroom = new Classroom();
        classroom.setName(name);
        classroom.setTeacher(((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());

        classroomsRepository.save(classroom);
        classroom.setStudents(new ArrayList<>());

        return classroom;
    }

    public List<Classroom> getClassroomsByTeacher(User teacher){
        return classroomsRepository.findAllByTeacher(teacher);
    }

    public Classroom getClassroomByIdAndCheckTeacher(UUID classroomId, User teacher){
        Optional<Classroom> classroom = classroomsRepository.findById(classroomId);

        checkIfClassroomHasSuchTeacherOtherwiseThrowException(classroom, teacher);

        return classroom.get();
    }

    public void checkIfClassroomHasSuchTeacherOtherwiseThrowException(UUID classroomId, User teacher) throws ClassroomException {
        Optional<Classroom> classroom = classroomsRepository.findById(classroomId);
        if(classroom.isEmpty()){
            throw new ClassroomException("Данного classroom не существует");
        }
        if(!classroom.get().getTeacher().getId().equals(teacher.getId())){
            throw new ClassroomException("Этот classroom не принадлежит этому учителю");
        }
    }

    public void checkIfClassroomHasSuchTeacherOtherwiseThrowException(Optional<Classroom> classroom, User teacher) throws ClassroomException {
        if(classroom.isEmpty()){
            throw new ClassroomException("Данного classroom не существует");
        }
        if(!classroom.get().getTeacher().getId().equals(teacher.getId())){
            throw new ClassroomException("Этот classroom не принадлежит этому учителю");
        }
    }

    public void deleteById(UUID classroomId){
        classroomsRepository.deleteById(classroomId);
    }

    @Transactional
    public void update(Classroom updatedClassroom){
        Optional<Classroom> classroomToBeUpdated = classroomsRepository.findById(updatedClassroom.getId());
        if(classroomToBeUpdated.isEmpty()){
            throw new ClassroomException("Такого classroom не существует, обновления не произошло");
        }

        classroomToBeUpdated.get().setName(updatedClassroom.getName());
    }

    @Transactional
    public void addStudentToClassroom(UUID classroomId, String studentLogin) {
        User teacher = ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Optional<Classroom> classroom = classroomsRepository.findById(classroomId);
        checkIfClassroomHasSuchTeacherOtherwiseThrowException(classroom, teacher);

        Optional<User> student = usersRepository.findByLogin(studentLogin);
        if(student.isEmpty()){
            throw  new ClassroomException("Студента с таким логином нет");
        }

        checkIfStudentIsNotInGroupOtherwiseThrowException(classroom.get(), student.get());

        classroom.get().getStudents().add(student.get());
    }

    @Transactional
    public void removeStudentFromClassroom(UUID classroomId, String studentLogin) {
        User teacher = ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Optional<Classroom> classroom = classroomsRepository.findById(classroomId);
        checkIfClassroomHasSuchTeacherOtherwiseThrowException(classroom, teacher);

        Optional<User> student = usersRepository.findByLogin(studentLogin);
        if(student.isEmpty()){
            throw  new ClassroomException("Студента с таким логином нет");
        }

        checkIfStudentIsInGroupOtherwiseThrowException(classroom.get(), student.get());

        classroom.get().getStudents().remove(student.get());
    }

    @Transactional
    @PreAuthorize("hasRole('PUPIL')")
    public void quitFromClassroom(UUID classroomId, User student) {
        Optional<Classroom> classroom = classroomsRepository.findById(classroomId);

        if(classroom.isEmpty()){
            throw new ClassroomException("Этого classroom не существует");
        }

        checkIfStudentIsInGroupOtherwiseThrowException(classroom.get(), student);

        classroom.get().getStudents().remove(student);
    }

    @Transactional
    @PreAuthorize("hasRole('PUPIL')")
    public void addStudentToRandomGroup(User student){
        List<Classroom> systemClassrooms = classroomsRepository.findAllByTeacher(systemBot);
        systemClassrooms = systemClassrooms
                .stream().filter(classroom -> classroom.getStudents().size() < MAX_COUNT_STUDENTS)
                .collect(Collectors.toList());

        Classroom classroom;
        if(systemClassrooms.isEmpty()){
            classroom = createByName("куянчики");
        }
        else{
            int randomIndex = random.nextInt(0, systemClassrooms.size());
            classroom = systemClassrooms.get(randomIndex);
        }
        classroom.getStudents().add(student);
    }

    private void checkIfStudentIsNotInGroupOtherwiseThrowException(Classroom classroom, User student) throws ClassroomException{
        if(classroom.getStudents().stream().anyMatch(s -> s.getId().equals(student.getId()))){
            throw new ClassroomException("Студент уже есть в списке группы");
        }
    }

    private void checkIfStudentIsInGroupOtherwiseThrowException(Classroom classroom, User student) throws ClassroomException{
        classroom.getStudents().stream().filter(s -> s.getId().equals(student.getId())).findAny().orElseThrow(
                () -> new ClassroomException("Студента нет в списке группы")
        );
    }

    @Transactional
    @PreAuthorize("hasRole('PUPIL')")
    public List<Classroom> getClassroomsByStudentId(UUID studentId) {
        Optional<User> student = usersRepository.findById(studentId);
        if(student.isEmpty()){
            throw new ClassroomException("Не существует такого пользователя");
        }

        return student.get().getClassrooms();
    }
}
