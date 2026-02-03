package com.anuj.repository;

import com.anuj.dto.ProductSearchDTO;
import com.anuj.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    @Query("""
    SELECT new com.anuj.dto.ProductSearchDTO(
        p.productId,
        p.name,
        p.brand,
        p.price,
        p.isAvailable,
        p.quantity,
        p.imageName
    )
    FROM Product p
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    public List<ProductSearchDTO> searchProducts(@Param("keyword") String keyword);

    List<Product> findByCategoryIgnoreCase(String category);
}
