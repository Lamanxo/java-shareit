package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {
        return userService.addUser(userDto);
    }

    @GetMapping("{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("{id}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        return userService.updateUser(userDto,id);
    }

    @DeleteMapping("{id}")
    public UserDto deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }


}
