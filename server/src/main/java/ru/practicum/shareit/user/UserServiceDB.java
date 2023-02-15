package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repo.UserRepository;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceDB implements UserService {

    private final UserRepository userRepo;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user;
        try {
            user = userRepo.save(UserMapper.makeUser(userDto));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("This email already used");
        }
        log.info("User {} created", user.getName());
        return UserMapper.makeUserDto(user);

    }

    @Override
    public UserDto getUser(Long id) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new UserNotFoundException("User with id: " + id + " not found"));
        log.info("Finding user with ID: {}", id);
        return UserMapper.makeUserDto(user);
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
        userRepo.save(UserMapper.makeUser(userDto1));
        return getUser(id);
    }

    @Override
    public UserDto deleteUser(Long id) {
        UserDto userDto = getUser(id);
        userRepo.deleteById(id);
        log.warn("Deleting User with ID: {}", id);
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : userRepo.findAll()) {
            userDtos.add(UserMapper.makeUserDto(user));
        }
        log.warn("Getting all users");
        return userDtos;
    }
}
