package ru.practicum.shareit.user.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.util.*;

@Component
@Slf4j
public class InMemUserStorage {

    private final Map<Long, User> userMap = new HashMap<>();

    private long userId = 0;

    public User addUser(User user) {
        emailCheck(user.getEmail());
        userId++;
        user.setId(userId);
        userMap.put(userId, user);
        log.info("User with id: {} was created", userId);
        return userMap.get(userId);
    }

    public User getUser(Long id) {
        return userMap.get(id);
    }

    public User updateUser(User user, Long id) {
        userMap.put(id, user);
        return userMap.get(id);
    }

    public User deleteUser(Long id) {
        User user = userMap.get(id);
        userMap.remove(id);
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    private void emailCheck(String email) {
        for (var user : userMap.values()) {
            if (user.getEmail().equals(email)) {
                throw new ValidationException("Email " + email + " already exist");
            }
        }
    }

}
