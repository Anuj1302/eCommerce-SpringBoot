package com.anuj.service;

import com.anuj.dto.UserProfileResponse;
import com.anuj.entity.RegisterUser;
import com.anuj.repository.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    RegisteredUserRepository registerUserRepo;

    public UserProfileResponse getProfile(String username) {
        RegisterUser user = registerUserRepo
                .findByUsername_Username(username)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return new UserProfileResponse(
                user.getUsername().getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getDob()
        );
    }
}

