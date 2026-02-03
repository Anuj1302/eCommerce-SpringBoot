package com.anuj.dto;

import com.anuj.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchDTO {

    private Integer productId;
    private String name;
    private String brand;
    private BigDecimal price;
    private Boolean isAvailable;
    private Integer quantity;
    private String imageName;

    public static ProductSearchDTO fromEntity(Product p) {
        ProductSearchDTO dto = new ProductSearchDTO();
        dto.setProductId(p.getProductId());
        dto.setName(p.getName());
        dto.setBrand(p.getBrand());
        dto.setPrice(p.getPrice());
        dto.setIsAvailable(p.getIsAvailable());
        dto.setQuantity(p.getQuantity());
        dto.setImageName(p.getImageName());
        return dto;
    }
}
