package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.repo.InMemUserStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("UserServiceMemory")
@RequiredArgsConstructor
@Slf4j
public class UserServiceMem implements UserService {
    private final InMemUserStorage userStorage;

    public UserDto addUser(UserDto userDto) {
        return makeUserDto(userStorage.addUser(makeUser(userDto)));
    }

    public UserDto getUser(Long id) {
        try {
            userStorage.getUser(id).getId();
        } catch (NullPointerException e) {
            throw new UserNotFoundException(String.format("User %s not found", id));
        }
        log.info("Getting user with id: {}", id);
        return makeUserDto(userStorage.getUser(id));
    }

    public UserDto updateUser(UserDto userDto, Long id) {
        checkUserEmail(userDto);
        try {
            userStorage.getUser(id).getId();
        } catch (NullPointerException e) {
            throw new UserNotFoundException(String.format("User %s not found", id));
        }
        User user = userStorage.getUser(id);
        userStorage.deleteUser(id);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        log.info("User with id: {} updated", id);
        return makeUserDto(userStorage.updateUser(user,id));
    }

    public UserDto deleteUser(Long id) {
        try {
            userStorage.getUser(id).getId();
        } catch (NullPointerException e) {
            throw new UserNotFoundException(String.format("User %s not found", id));
        }
        log.info("User with id: {} removed", id);
        return makeUserDto(userStorage.deleteUser(id));
    }

    public List<UserDto> getAllUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        for (var user : userStorage.getAllUsers()) {
            usersDto.add(makeUserDto(user));
        }
        return usersDto;
    }

    private User makeUser(UserDto userDto) {
        return User.builder()
            .id(userDto.getId())
            .name(userDto.getName())
            .email(userDto.getEmail())
            .build();
    }

    private UserDto makeUserDto(User user) {
        return UserDto.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .build();
    }

    private void checkUserEmail(UserDto userDto) {
        for (var user : getAllUsers()) {
            if (user.getEmail().equals(userDto.getEmail())) {
                throw new ValidationException("Email " + userDto.getEmail() + " already exist");
            }
        }
    }
}
