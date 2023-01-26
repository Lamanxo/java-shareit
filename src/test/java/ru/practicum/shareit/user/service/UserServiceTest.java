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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceDB userService;
    @Mock
    private UserRepository userRepo;

    @Test
    void addUser_checkReturned() {
        User user = new User();

        when(userRepo.save(any()))
                .thenReturn(user);

        UserDto userDto = new UserDto();
        UserDto userDto1 = userService.addUser(userDto);
        assertEquals(userDto1, userDto);
    }

}