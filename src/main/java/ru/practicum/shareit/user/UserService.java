package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;

import javax.validation.ValidationException;
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
        checkUserEmail(userDto);
        if(userStorage.getUser(id).getId() != id) {
            new UserNotFoundException(String.format("User %s not found", id));
        }
        User user = userStorage.getUser(id);
        userStorage.deleteUser(id);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        return makeUserDto(userStorage.updateUser(user,id));
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

    private void checkUserEmail(UserDto userDto) {
        for (var user : getAllUsers()) {
            if (user.getEmail().equals(userDto.getEmail())) {
                throw new ValidationException("Email " + userDto.getEmail() + " already exist");
            }
        }
    }
}
