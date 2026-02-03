package com.anuj.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private Users user;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "productId",
            foreignKey = @ForeignKey(name = "fk_cart_product")
    )
    private Product product;

    private int quantity;
}

