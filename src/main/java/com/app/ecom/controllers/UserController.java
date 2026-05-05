package com.app.ecom.controllers;

import com.app.ecom.services.UserServices;
import com.app.ecom.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserServices userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.fetchAllUsers(),HttpStatus.OK);
    }
    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) {
        return userService.fetchUser(userId).
                map(ResponseEntity::ok).orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/api/users")
    public ResponseEntity<String> addNewUser(@RequestBody User user) {
        userService.addUser(user);
        return ResponseEntity.ok("user added");
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        boolean updated= userService.updateUser(id, updatedUser);
        if (updated) {
            return ResponseEntity.ok("user updated");
        } else {
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        }
    }
}
