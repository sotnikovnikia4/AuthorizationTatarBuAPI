package ru.codecrafters.AuthorizationTatarBuAPI.services;

import com.sotnikov.ListToDoBackend.exceptions.UserDataNotChangedException;
import com.sotnikov.ListToDoBackend.models.User;
import com.sotnikov.ListToDoBackend.repotitories.TasksRepository;
import com.sotnikov.ListToDoBackend.repotitories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;
    private final TasksRepository tasksRepository;

    public Optional<User> findOne(String login){
        return usersRepository.findByLogin(login);
    }

    @Transactional
    public void update(User updatedUser, UUID id){
        Optional<User> userToUpdate = usersRepository.findById(id);

        if(userToUpdate.isPresent()){
            userToUpdate.get().setName(updatedUser.getName());
            userToUpdate.get().setPassword(updatedUser.getPassword());
            userToUpdate.get().setLogin(updatedUser.getLogin());
        }
        else{
            throw new UserDataNotChangedException("This user does not exist");
        }
    }

    @Transactional
    public void delete(User user){
        tasksRepository.deleteAllByUserId(user.getId());

        usersRepository.delete(user);
    }
}
