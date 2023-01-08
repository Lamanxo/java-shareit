package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repo.UserRepository;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
@Service
@Qualifier("UserServiceDB")
@Slf4j
@RequiredArgsConstructor
public class UserServiceDB implements UserService {

    private final UserRepository userRepo;
    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        User user;
        try {
            user = userRepo.save(makeUser(userDto));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("This email already used");
        }
        return makeUserDto(user);

    }

    @Override
    public UserDto getUser(Long id) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new UserNotFoundException("User with id: " + id + " not found"));

        return makeUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        UserDto userDto1 = getUser(id);
        if (userDto.getName() != null) {
            userDto1.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userDto1.setEmail(userDto.getEmail());
        }
        log.info("User with id: {} updated", id);
        userRepo.save(makeUser(userDto1));
        return getUser(id);
    }

    @Override
    public UserDto deleteUser(Long id) {
        UserDto userDto = getUser(id);
        userRepo.deleteById(id);
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : userRepo.findAll()) {
            userDtos.add(makeUserDto(user));
        }
        return userDtos;
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
}
