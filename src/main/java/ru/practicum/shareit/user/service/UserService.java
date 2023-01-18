package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto getUser(Long id);

    UserDto updateUser(UserDto userDto, Long id);

    UserDto deleteUser(Long id);

    List<UserDto> getAllUsers();
}
