package com.anuj.controller;

import com.anuj.dto.CreateProductDTO;
import com.anuj.dto.ProductSearchDTO;
import com.anuj.entity.Product;
import com.anuj.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/getProducts")
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) String category
    ) {
        List<Product> products;
        if(category == null || category.isBlank()){
            products = productService.getProducts();
        }else{
            products = productService.getProductsByCategory(category);
        }

        return ResponseEntity.ok(products);
    }

    @GetMapping("/getProducts/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
//        if(product == null){
//            return ResponseEntity.noContent().build();
//        }
//        return productService.getProductById(id);
//        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(
            @RequestPart("product") Product product,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            Product savedProduct = productService.addProduct(product, image);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable("productId") int id){
        Optional<Product> product = productService.getProductById(id);
        if(!product.isPresent()){
            return ResponseEntity.ok(null);
        }
        byte[] imageFile = product.get().getImageData();
        if (product.get().getImageData() == null || product.get().getImageType() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.get().getImageType()))
                .body(imageFile);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable int id,
            @RequestPart("product") Product product,
            @RequestPart(value = "image", required = false) MultipartFile image){
        try {
            productService.updateProduct(id,product, image);
            return new ResponseEntity<>(HttpStatus.OK);
//            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        productService.deleteProd(id);
        return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
    }
    @GetMapping("/products/search")
    public ResponseEntity<List<ProductSearchDTO>> searchProducts(@RequestParam  String keyword){

        List<ProductSearchDTO> list = productService.getSearchProducts(keyword);
//        if(list.isEmpty()){
//            return ResponseEntity.noContent().build();
//        }
        return ResponseEntity.ok(list);
    }

}
