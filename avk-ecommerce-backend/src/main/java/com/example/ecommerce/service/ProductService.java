package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll(String category, String search) {
        if (search != null && !search.isBlank()) {
            return repository.findByNameContainingIgnoreCase(search);
        }
        if (category != null && !category.isBlank()) {
            return repository.findByCategoryIgnoreCase(category);
        }
        return repository.findAll();
    }

    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    public Product create(ProductRequest request) {
        Product p = new Product();
        copy(request, p);
        return repository.save(p);
    }

    public Product update(Long id, ProductRequest request) {
        Product p = getById(id);
        copy(request, p);
        return repository.save(p);
    }

    public void delete(Long id) {
        Product p = getById(id);
        repository.delete(p);
    }

    private void copy(ProductRequest r, Product p) {
        p.setName(r.name());
        p.setDescription(r.description());
        p.setPrice(r.price());
        p.setStock(r.stock());
        p.setCategory(r.category());
        p.setImageUrl(r.imageUrl());
    }
}
