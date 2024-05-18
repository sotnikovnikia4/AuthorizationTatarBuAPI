package ru.codecrafters.AuthorizationTatarBuAPI.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.codecrafters.AuthorizationTatarBuAPI.exceptions.UserDataNotChangedException;
import ru.codecrafters.AuthorizationTatarBuAPI.models.User;
import ru.codecrafters.AuthorizationTatarBuAPI.repotitories.UsersRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;

    public Optional<User> findOne(String login){
        return usersRepository.findByLogin(login);
    }

    @Transactional
    public void update(User updatedUser, UUID id){//TODO
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
    public void delete(User user){//TODO
        usersRepository.delete(user);
    }
}
