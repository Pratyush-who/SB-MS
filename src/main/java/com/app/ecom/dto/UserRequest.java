package com.app.ecom.dto;

import com.app.ecom.models.UserRole;
import lombok.Data;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private AddressDto address;
}
