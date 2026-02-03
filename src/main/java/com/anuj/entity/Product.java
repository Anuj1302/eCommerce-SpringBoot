package com.anuj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId ;
    private String name;
    private String description;
    private String brand;
    private String category;
    private BigDecimal price;
    private Date releaseDate;
    private Boolean isAvailable;
    private Integer quantity;
    private String imageName;
    private String imageType;
    @Lob
    @Column(columnDefinition = "BYTEA")
//    @JsonIgnore
    private byte[] imageData;
}

