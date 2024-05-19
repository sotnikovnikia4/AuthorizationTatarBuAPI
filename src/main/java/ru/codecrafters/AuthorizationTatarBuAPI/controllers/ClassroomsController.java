package ru.codecrafters.AuthorizationTatarBuAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.codecrafters.AuthorizationTatarBuAPI.dto.ChangingClassroomDTO;
import ru.codecrafters.AuthorizationTatarBuAPI.dto.ClassroomDTO;
import ru.codecrafters.AuthorizationTatarBuAPI.dto.CreationClassroomDTO;
import ru.codecrafters.AuthorizationTatarBuAPI.exceptions.ClassroomException;
import ru.codecrafters.AuthorizationTatarBuAPI.models.Classroom;
import ru.codecrafters.AuthorizationTatarBuAPI.models.User;
import ru.codecrafters.AuthorizationTatarBuAPI.security.UserDetailsImpl;
import ru.codecrafters.AuthorizationTatarBuAPI.services.ClassroomsService;
import ru.codecrafters.AuthorizationTatarBuAPI.util.ErrorMessageMaker;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/classrooms")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
public class ClassroomsController {
    private final ClassroomsService classroomsService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @PostMapping("/create")
    public ResponseEntity<ClassroomDTO> createClassroom(@RequestBody CreationClassroomDTO creationClassroomDTO, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new ClassroomException(ErrorMessageMaker.formErrorMessage(bindingResult));
        }

        Classroom createdClassroom = classroomsService.createByName(creationClassroomDTO.getName());

        System.out.println(convertToClassroomDTO(createdClassroom).getTeacher());

        return new ResponseEntity<>(convertToClassroomDTO(createdClassroom), HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ClassroomDTO>> getAll(){
        User teacher = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        List<Classroom> classrooms = classroomsService.getClassroomsByTeacher(teacher);

        return new ResponseEntity<>(
                classrooms.stream().map(this::convertToClassroomDTO).collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @GetMapping("/get-one")
    public ResponseEntity<ClassroomDTO> get(@RequestParam(name = "classroom_id") UUID classroomId){
        User teacher = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Classroom classroom = classroomsService.getClassroomByIdAndCheckTeacher(classroomId, teacher);

        return new ResponseEntity<>(
                convertToClassroomDTO(classroom),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam(name = "classroom_id") UUID classroomId){
        User teacher = ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        classroomsService.checkIfClassroomHasSuchTeacherOtherwiseThrowException(classroomId, teacher);

        classroomsService.deleteById(classroomId);
    }

    @PatchMapping("/edit")
    public void edit(@RequestBody @Valid ChangingClassroomDTO changingClassroomDTO, BindingResult bindingResult){
        User teacher = ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if(bindingResult.hasErrors()){
            throw new ClassroomException(ErrorMessageMaker.formErrorMessage(bindingResult));
        }

        Classroom classroom = convertToClassroom(changingClassroomDTO);
        classroom.setTeacher(teacher);

        classroomsService.checkIfClassroomHasSuchTeacherOtherwiseThrowException(classroom.getId(), teacher);

        classroomsService.update(classroom);
    }

    @PutMapping ("/add-student")
    @ResponseStatus(HttpStatus.OK)
    public void addStudent(@RequestParam(name = "classroom_id") UUID classroomId,
                           @RequestParam(name = "student_login") String studentLogin
    ){

        classroomsService.addStudentToClassroom(classroomId, studentLogin);
    }

    @DeleteMapping("/remove-student")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStudent(@RequestParam(name = "classroom_id") UUID classroomId,
                           @RequestParam(name = "student_login") String studentLogin
    ){

        classroomsService.removeStudentFromClassroom(classroomId, studentLogin);
    }

    @DeleteMapping("/quit")
    @ResponseStatus(HttpStatus.OK)
    public void quit(@RequestParam(name = "classroom_id") UUID classroomId){
        User student = ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        classroomsService.quitFromClassroom(classroomId, student);
    }

    @PutMapping("/enter-to-random-system-group")
    public void enter(){
        User user = ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        classroomsService.addStudentToRandomGroup(user);
    }

    @GetMapping("/ask-join-group")
    @ResponseStatus(HttpStatus.OK)
    public String askJoinGroup(){
        return "Учитель должен добавить вас по логину, обратитесь к учителю";
    }

    @GetMapping("/student/{studentId}/get-classrooms")
    public ResponseEntity<List<ClassroomDTO>> classroomDTOS (@PathVariable UUID studentId){

        List<Classroom> classrooms = classroomsService.getClassroomsByStudentId(studentId);

        return new ResponseEntity<>(
                classrooms.stream().map(this::convertToClassroomDTO).collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    private ClassroomDTO convertToClassroomDTO(Classroom classroom){
        return modelMapper.map(classroom, ClassroomDTO.class);
    }

    private Classroom convertToClassroom(ChangingClassroomDTO changingClassroomDTO){
        return modelMapper.map(changingClassroomDTO, Classroom.class);
    }
}
