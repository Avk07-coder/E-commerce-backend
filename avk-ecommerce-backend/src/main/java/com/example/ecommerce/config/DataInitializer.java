package com.example.ecommerce.config;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(UserRepository userRepository,
                               ProductRepository productRepository,
                               PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByEmail("admin@ecommerce.com")) {
                userRepository.save(new User(
                        "Admin",
                        "admin@ecommerce.com",
                        passwordEncoder.encode("Admin@123"),
                        Role.ADMIN
                ));
            }

            if (!userRepository.existsByEmail("user@ecommerce.com")) {
                userRepository.save(new User(
                        "Demo User",
                        "user@ecommerce.com",
                        passwordEncoder.encode("User@123"),
                        Role.USER
                ));
            }

            if (productRepository.count() == 0) {

                add(
                        productRepository,
                        "iPhone 15",
                        "Apple smartphone",
                        "69999",
                        20,
                        "Electronics",
                        "https://example.com/iphone.jpg"
                );

                add(
                        productRepository,
                        "Samsung Galaxy S24",
                        "Samsung smartphone",
                        "64999",
                        25,
                        "Electronics",
                        "https://example.com/s24.jpg"
                );

                add(
                        productRepository,
                        "Nike Air Max",
                        "Comfortable running shoes",
                        "7999",
                        30,
                        "Shoes",
                        "https://example.com/nike.jpg"
                );

                add(
                        productRepository,
                        "Laptop Backpack",
                        "Water resistant backpack",
                        "1499",
                        50,
                        "Bags",
                        "https://example.com/bag.jpg"
                );
            }
        };
    }

    private void add(ProductRepository repo,
                     String name,
                     String desc,
                     String price,
                     int stock,
                     String category,
                     String imageUrl) {

        Product p = new Product();

        p.setName(name);
        p.setDescription(desc);
        p.setPrice(new BigDecimal(price));
        p.setStock(stock);
        p.setCategory(category);
        p.setImageUrl(imageUrl);

        repo.save(p);
    }
}