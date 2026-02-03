package com.anuj.controller;

import com.anuj.entity.Categories;
import com.anuj.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryRepo categoryRepo;

    @RequestMapping("/categories")
    public ResponseEntity<List<Categories>> getAllCategories(){
        List<Categories> cat = categoryRepo.findAll();

        return ResponseEntity.ok(cat);
    }

}
