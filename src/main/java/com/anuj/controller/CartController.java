package com.anuj.controller;

import com.anuj.entity.Cart;
import com.anuj.entity.Product;
import com.anuj.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addToCart(
            @PathVariable Integer productId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        cartService.addToCart(username, productId);
        return ResponseEntity.ok("Added to cart");
    }

    @GetMapping()
    public ResponseEntity<List<Cart>> getCart(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(cartService.getUserCart(username));
    }


    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @PathVariable Integer productId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        cartService.removeFromCart(username, productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/increase/{productId}")
    public ResponseEntity<?> increaseQuantity(@PathVariable Integer productId, Authentication authentication){
        cartService.increaseQuant(authentication.getName(), productId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/decrease/{productId}")
    public ResponseEntity<Void> decrease(
            @PathVariable Integer productId,
            Authentication authentication) {

        cartService.decreaseQuantity(authentication.getName(), productId);
        return ResponseEntity.ok().build();
    }
}
