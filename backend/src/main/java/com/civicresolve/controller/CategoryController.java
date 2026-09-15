package com.civicresolve.controller;
import com.civicresolve.repository.CategoryRepository; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/categories") public class CategoryController{
 private final CategoryRepository repo;public CategoryController(CategoryRepository r){repo=r;}
 @GetMapping public Object all(){return repo.findAll();}
}