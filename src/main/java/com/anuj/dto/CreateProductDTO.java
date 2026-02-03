package com.anuj.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CreateProductDTO {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String brand;

    @NotBlank
    private String category;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    private Date releaseDate;

    private Boolean isAvailable;

    @NotNull
    @PositiveOrZero
    private Integer quantity;
}
