package com.app.ecom;

import lombok.Data;
import lombok.Getter;

@Data
public class User {
    private long  id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
