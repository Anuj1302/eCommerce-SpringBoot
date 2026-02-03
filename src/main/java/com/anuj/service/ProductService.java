package com.anuj.service;

import com.anuj.dto.CreateProductDTO;
import com.anuj.dto.ProductSearchDTO;
import com.anuj.entity.Product;
import com.anuj.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService{
    @Autowired
    ProductRepo productRepo;

//    List<Product> productList = new ArrayList<>(
//            Arrays.asList(
//                    new Product(101, "iPhone 17", 120000),
//                    new Product(102, "Watch", 9000),
//                    new Product(103, "Bag", 600)
//            )
//    );

    @Transactional(readOnly = true) //No need here since no joins or any complex query from other entity but still for defensive
    public List<Product> getProducts() {

        List<Product> products = productRepo.findAll();
        return products;
    }
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String category) {

        List<Product> products = productRepo.findByCategoryIgnoreCase(category);
        return products;
    }

    public Optional<Product> getProductById(int id){
//        return productList.stream()
//                .filter(product -> product.getProdId() == id)
//                .findFirst()
//                .orElse(new Product(404, "XYZ", 90));
        return productRepo.findById(id);
    }

    public Product addProduct(Product product, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            product.setImageName(image.getOriginalFilename());
            product.setImageType(image.getContentType());
            product.setImageData(image.getBytes());
        }
        return productRepo.save(product);
    }


    public void updateProduct(int id, Product updatedProduct,MultipartFile image) throws  IOException{
        Product existingProduct = productRepo.findById(id).orElseThrow(() -> new RuntimeException(("Product Not Found")));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setBrand(updatedProduct.getBrand());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setReleaseDate(updatedProduct.getReleaseDate());
        existingProduct.setIsAvailable(updatedProduct.getIsAvailable());
        existingProduct.setCategory(updatedProduct.getCategory());

        if(image != null && !image.isEmpty()){
            existingProduct.setImageName(image.getOriginalFilename());
            existingProduct.setImageType(image.getContentType());
            existingProduct.setImageData(image.getBytes());
        }
        productRepo.save(existingProduct);
    }

    public void deleteProd(int id) {
        productRepo.deleteById(id);
    }
    @Transactional(readOnly = true)
    public List<ProductSearchDTO> getSearchProducts(String keyword) {
        return productRepo.searchProducts(keyword);
    }

}
