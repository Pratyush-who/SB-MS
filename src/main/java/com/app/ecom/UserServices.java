package com.app.ecom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServices {
    private final UserRepository userRepository;
//    private final List<User> userList = new ArrayList<>();
    private Long nextId = 1L;

    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
//        user.setId(nextId++);
        userRepository.save(user);
    }

    public Optional<User> fetchUser(Long id) {
        return userRepository.findById(id);
    }

    public boolean updateUser(Long id, User updateduser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(updateduser.getFirstName());
                    user.setLastName(updateduser.getLastName());
                    userRepository.save(user);  
                    return true;
                }).orElse(false);
    }
}
