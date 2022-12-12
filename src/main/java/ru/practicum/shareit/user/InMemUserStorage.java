package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.*;

@Component
public class InMemUserStorage {

    Map<Long, User> userMap = new HashMap<>();

    private long userId = 0;

    public User addUser(User user) {
        emailCheck(user.getEmail());
        userId++;
        user.setId(userId);
        userMap.put(userId, user);
        return userMap.get(userId);
    }

    public User getUser(Long id) {
        return userMap.get(id);
    }

    public User updateUser(User user, Long id) {
        User user1 = userMap.get(id);
        if(user.getId() != null) {
            user1.setId(user.getId());
        } if(user.getName() != null) {
            user1.setName(user.getName());
        } if(user.getEmail() != null) {
            user1.setEmail(user.getEmail());
        }
        userMap.remove(id);
        emailCheck(user.getEmail());
        userMap.put(id, user1);
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
