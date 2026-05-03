package com.app.ecom;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServices {
    private final List<User> userList = new ArrayList<>();
    private Long nextId = 1L;

    public List<User> fetchAllUsers() {
        return userList;
    }

    public void addUser(User user) {
        user.setId(nextId++);
        userList.add(user);
    }

    public Optional<User> fetchUser(Long id) {
        return userList.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    public boolean updateUser(Long id, User updateduser) {
        return userList.stream().
                filter(user -> user.getId() == id)
                .findFirst()
                .map(user -> {
                    user.setFirstName(updateduser.getFirstName());
                    user.setLastName(updateduser.getLastName());
                    user.setEmail(updateduser.getEmail());
                    user.setPassword(updateduser.getPassword());
                    return true;
                }).orElse(false);
    }
}
