package com.app.ecom.services;

import com.app.ecom.dto.AddressDto;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.models.Address;
import com.app.ecom.models.User;
import com.app.ecom.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServices {
    private final UserRepository userRepository;
//    private final List<User> userList = new ArrayList<>();
    private Long nextId = 1L;

    public List<UserResponse> fetchAllUsers() {
        List<User> users = userRepository.findAll();
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void addUser(UserRequest userRequest) {
//        user.setId(nextId++);
        User user =new  User();
        updateUserFromRequest(user, userRequest);
        userRepository.save(user);
    }

    public Optional<User> fetchUser(Long id) {
        return userRepository.findById(id);
    }

    public boolean updateUser(Long id, UserRequest updatedUserRequest) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserFromRequest(existingUser, updatedUserRequest);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        if (userRequest.getAddress() != null) {
            Address addressDto = new  Address();
            addressDto.setStreet(userRequest.getAddress().getStreet());
            addressDto.setCity(userRequest.getAddress().getCity());
            addressDto.setState(userRequest.getAddress().getState());
            addressDto.setZipcode(userRequest.getAddress().getZipcode());
            addressDto.setCountry(userRequest.getAddress().getCountry());
        }
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRole(user.getRole());
        userResponse.setAddress(new  AddressDto());
        if (user.getAddress() != null) {
            AddressDto addressDto = new AddressDto();
            addressDto.setStreet(user.getAddress().getStreet());
            addressDto.setCity(user.getAddress().getCity());
            addressDto.setState(user.getAddress().getState());
            addressDto.setZipcode(user.getAddress().getZipcode());
            userResponse.setAddress(addressDto);
        }
        return userResponse;
    }

}
