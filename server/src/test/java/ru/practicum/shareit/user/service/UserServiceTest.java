package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserServiceDB;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceDB userService;
    @Mock
    private UserRepository userRepo;

    @Test
    void addUser_checkReturnedTest() {
        User user = new User();

        when(userRepo.save(any()))
                .thenReturn(user);

        UserDto userDto = new UserDto();
        UserDto userDto1 = userService.addUser(userDto);
        assertEquals(userDto1, userDto);
    }

    @Test
    void updateUserTest() {
        User user = new User();
        user.setName("Name2");
        when(userRepo.findById(anyLong()))
                .thenReturn(Optional.of(user));
        UserDto userDto = new UserDto();
        userDto.setName("Name2");
        UserDto returnedDto = userService.updateUser(userDto, 1L);
        assertEquals(returnedDto, userDto);
    }

    @Test
    void getUserTest() {
        User user = new User();
        UserDto userDto = new UserDto();
        when(userRepo.findById(0L))
                .thenReturn(Optional.of(user));

        UserDto returnedDto = userService.getUser(0L);

        assertEquals(userDto, returnedDto);
    }

    @Test
    void getAllUsersTest() {
        User user = new User();
        UserDto usersDto = new UserDto();
        when(userRepo.findAll()).thenReturn(List.of(user));

        List<UserDto> returnedUsersDto = userService.getAllUsers();
        assertEquals(1, returnedUsersDto.size());
        assertEquals(usersDto, returnedUsersDto.get(0));
    }

    @Test
    void deleteUserTest() {
        User user = new User();
        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));
        userService.deleteUser(1L);

        verify(userRepo, times(1)).deleteById(1L);
    }

}