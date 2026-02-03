package com.anuj.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "registered_user")
public class RegisterUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    private LocalDate dob;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users username;
}
