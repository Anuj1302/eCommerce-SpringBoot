package com.anuj.controller;

import com.anuj.dto.AuthResponse;
import com.anuj.dto.RefreshRequest;
import com.anuj.dto.RegisterRequest;
import com.anuj.dto.UserProfileResponse;
import com.anuj.entity.Users;
import com.anuj.service.JWTService;
import com.anuj.service.MyUserDetailsService;
import com.anuj.service.ProfileService;
import com.anuj.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ProfileService profileService;

    @Autowired
    JWTService jwtService;

    @Autowired
    ApplicationContext context;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest user){
        userService.registerUser(user);
        return ResponseEntity.ok("User Registered");
    }

//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> login(@RequestBody Users user){
//        AuthResponse response = new AuthResponse();
//        response.setRole(userService.getRole(user.getUsername()));
//        response.setToken(userService.verify(user));
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody Users request,
            HttpServletResponse response
    ) {
        Users user = userService.authenticate(request.getUsername(), request.getPassword());

        String accessToken = jwtService.generateToken(user.getUsername(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        // 🔒 HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // true in prod (HTTPS)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax") // or Lax
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(
                new AuthResponse(accessToken, user.getRole())
        );
    }



    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(
            Authentication authentication) {

        System.out.println("Came in profile ");
        String username = authentication.getName(); // from JWT
        return ResponseEntity.ok(profileService.getProfile(username));
    }

//    @PostMapping("/refresh")
//    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
//
//        String username = jwtService.extractUserName(request.getRefreshToken());
//
//        UserDetails userDetails =context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
//        if (!jwtService.validateToken(request.getRefreshToken(), userDetails)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
////        String newRefreshToken = jwtService.generateRefreshToken(username);
//
//        String role = userService.getRole(username);
//        String newAccessToken = jwtService.generateToken(username, role);
//
////        AuthResponse response = new AuthResponse();
////        response.setToken(newAccessToken);
////        response.setRole(role);
////        response.setRefreshToken(newRefreshToken);
//
//        return ResponseEntity.ok(new AuthResponse(newAccessToken, role));
//    }

@PostMapping("/refresh")
public ResponseEntity<AuthResponse> refresh(
        HttpServletRequest request,
        HttpServletResponse response
) {
    Cookie[] cookies = request.getCookies();
    System.out.println("Cookies: " + Arrays.toString(request.getCookies()));
    if (cookies == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String refreshToken = Arrays.stream(cookies)
            .filter(c -> c.getName().equals("refreshToken"))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);

    if (refreshToken == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 1️⃣ Extract username from refresh token
    String username;
    try {
        username = jwtService.extractUserName(refreshToken);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 2️⃣ Load user from DB
    Users user = userService.findByUsername(username);

    // 3️⃣ Validate refresh token
    if (!jwtService.validateRefreshToken(refreshToken, user)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 4️⃣ Generate NEW tokens
    String role = user.getRole();
    String newAccessToken = jwtService.generateToken(username, role);
    String newRefreshToken = jwtService.generateRefreshToken(username);

    // 5️⃣ Rotate refresh token (HttpOnly cookie)
    ResponseCookie newCookie = ResponseCookie.from("refreshToken", newRefreshToken)
            .httpOnly(true)
            .secure(false) // set false only for local HTTP testing
            .path("/")
            .maxAge(Duration.ofDays(7))
            .sameSite("Lax")
            .build();

    response.addHeader(HttpHeaders.SET_COOKIE, newCookie.toString());

    // 6️⃣ Return ONLY access token + role
    return ResponseEntity.ok(
            new AuthResponse(newAccessToken, role)
    );
}






}
