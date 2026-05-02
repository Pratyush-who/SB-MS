package com.app.ecom;

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
        User user= userService.fetchUser(userId);
        if(user==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }
    @PostMapping("/api/users")
    public ResponseEntity<String> addNewUser(@RequestBody User user) {
        userService.addUser(user);
        return ResponseEntity.ok("user added");
    }
}
