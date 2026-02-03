package com.anuj.dto;

import java.time.LocalDate;

public record UserProfileResponse(
        String username,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDate dob
) {}
