package com.anuj.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String email;
    private String phoneNumber;

    private String username;
    private String password;
}
