package com.anuj.service;

import com.anuj.dto.RegisterRequest;
import com.anuj.entity.RegisterUser;
import com.anuj.entity.Users;
import com.anuj.repository.RegisteredUserRepository;
import com.anuj.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWTService jwtService;

    @Autowired
    RegisteredUserRepository registerUserRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    public void registerUser(RegisterRequest userReq) {
        if (userRepo.findByUsername(userReq.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Users user = new Users();
        user.setUsername(userReq.getUsername());
        user.setPassword(encoder.encode(userReq.getPassword()));
        user =userRepo.save(user);

        RegisterUser profile = new RegisterUser();
        profile.setDob(userReq.getDob());
        profile.setEmail(userReq.getEmail());
        profile.setFirstName(userReq.getFirstName());
        profile.setLastName(userReq.getLastName());
        profile.setUsername(user);

        registerUserRepo.save(profile);
    }


//    public String verify(Users user) {
//        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
//        if(authentication.isAuthenticated()){
//            return jwtService.generatekey(user.getUsername(), getRole(user.getUsername()));
//        }
//        return "Failed";
//    }

    public String getRole(String username) {
        Optional<Users> users = userRepo.findByUsername(username);
        if(!users.isPresent()){
            throw new RuntimeException("User Does not exist");
        }
        return users.get().getRole();
    }

    public Users authenticate(String username, String password) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("Invalid credentials");
        }

        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Users findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean validateRefreshToken(String token, Users user) {
        final String username = jwtService.extractUserName(token);
        return username.equals(user.getUsername()) && !jwtService.validateToken(token, (UserDetails) user);
    }




}
