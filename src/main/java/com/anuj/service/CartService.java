package com.anuj.service;

import com.anuj.entity.Cart;
import com.anuj.entity.Product;
import com.anuj.entity.Users;
import com.anuj.repository.CartRepository;
import com.anuj.repository.ProductRepo;
import com.anuj.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    public void addToCart(String username, Integer productId) {

        Users user = userRepo.findByUsername(username)
                .orElseThrow();

        Product product = productRepo.findById(productId)
                .orElseThrow();

        Cart cart = cartRepo.findByUserAndProduct(user, product)
                .orElse(new Cart());

        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(cart.getQuantity() + 1);

        cartRepo.save(cart);
    }

    public List<Cart> getUserCart(String username) {
        Users user = userRepo.findByUsername(username).orElseThrow();
        return cartRepo.findByUser(user);
    }

    public void removeFromCart(String username, Integer productId) {
        Users userr = userRepo.findByUsername(username).orElseThrow();
        Product prod = productRepo.findById(productId).orElseThrow();
        Cart cart = cartRepo
                .findByUserAndProduct(userr, prod)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartRepo.delete(cart);
    }

    @Transactional
    public void increaseQuant(String userName, Integer productId) {
        Users userr = userRepo.findByUsername(userName).orElseThrow();
        Product prod = productRepo.findById(productId).orElseThrow();
        Cart cart = cartRepo.findByUserAndProduct(userr, prod).orElseThrow();

        cart.setQuantity(cart.getQuantity() + 1);
    }

    @Transactional
    public void decreaseQuantity(String username, Integer productId) {
        Users userr = userRepo.findByUsername(username).orElseThrow();
        Product prod = productRepo.findById(productId).orElseThrow();
        Cart cart = cartRepo
                .findByUserAndProduct(userr, prod)
                .orElseThrow(() -> new RuntimeException("Item not in cart"));

        if (cart.getQuantity() <= 1) {
            cartRepo.delete(cart);
        } else {
            cart.setQuantity(cart.getQuantity() - 1);
        }
    }

}
