package com.anuj.repository;

import com.anuj.entity.Cart;
import com.anuj.entity.Product;
import com.anuj.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface
CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserAndProduct(Users user, Product product);

    List<Cart> findByUser(Users user);
}
