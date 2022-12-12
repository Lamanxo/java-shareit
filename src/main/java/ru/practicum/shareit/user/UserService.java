package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final InMemUserStorage userStorage;

    public UserDto addUser(UserDto userDto) {
        return makeUserDto(userStorage.addUser(makeUser(userDto)));
    }

    public UserDto getUser(Long id) {
        return makeUserDto(userStorage.getUser(id));
    }

    public UserDto updateUser(UserDto userDto, Long id) {
        return makeUserDto(userStorage.updateUser(makeUser(userDto), id));
    }

    public UserDto deleteUser(Long id) {
        return makeUserDto(userStorage.deleteUser(id));
    }

    public List<UserDto> getAllUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        for(var user : userStorage.getAllUsers()) {
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
}
