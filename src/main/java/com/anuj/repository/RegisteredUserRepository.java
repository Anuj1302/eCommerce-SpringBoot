package com.anuj.repository;

import com.anuj.entity.RegisterUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisterUser, Long>{

    boolean existsByUsername(String username);
    Optional<RegisterUser> findByUsername_Username(String username);
}
