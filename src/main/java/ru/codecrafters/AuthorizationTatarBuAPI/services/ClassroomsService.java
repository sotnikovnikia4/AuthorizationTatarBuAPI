package ru.codecrafters.AuthorizationTatarBuAPI.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.codecrafters.AuthorizationTatarBuAPI.exceptions.ClassroomException;
import ru.codecrafters.AuthorizationTatarBuAPI.models.Classroom;
import ru.codecrafters.AuthorizationTatarBuAPI.models.User;
import ru.codecrafters.AuthorizationTatarBuAPI.repotitories.ClassroomsRepository;
import ru.codecrafters.AuthorizationTatarBuAPI.repotitories.UsersRepository;
import ru.codecrafters.AuthorizationTatarBuAPI.security.UserDetailsImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassroomsService {
    private final ClassroomsRepository classroomsRepository;
    private final UsersRepository usersRepository;

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
        if(!student.get().getRole().getName().equals("PUPIL")){
            throw  new ClassroomException("У студента должна быть роль PUPIL");
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
    public void quitFromClassroom(UUID classroomId, User student) {
        Optional<Classroom> classroom = classroomsRepository.findById(classroomId);

        if(classroom.isEmpty()){
            throw new ClassroomException("Этого classroom не существует");
        }

        checkIfStudentIsInGroupOtherwiseThrowException(classroom.get(), student);

        classroom.get().getStudents().remove(student);
    }

    public void addStudentToRandomGroup(User student){
        List<Classroom> systemClassrooms = 
    }

    private void checkIfStudentIsNotInGroupOtherwiseThrowException(Classroom classroom, User student) throws ClassroomException{
        if(classroom.getStudents().stream().filter(s -> s.getId().equals(student.getId())).findAny().isPresent()){
            throw new ClassroomException("Студент уже есть в списке группы");
        }
    }

    private void checkIfStudentIsInGroupOtherwiseThrowException(Classroom classroom, User student) throws ClassroomException{
        classroom.getStudents().stream().filter(s -> s.getId().equals(student.getId())).findAny().orElseThrow(
                () -> new ClassroomException("Студента нет в списке группы")
        );
    }
}
