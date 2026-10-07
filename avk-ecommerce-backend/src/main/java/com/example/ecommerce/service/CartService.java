package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartRequest;
import com.example.ecommerce.dto.UpdateCartRequest;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CartService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartRepository;

    public CartService(UserRepository userRepository, ProductRepository productRepository,
                       CartItemRepository cartRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    public List<CartItem> getCart(String email) {
        return cartRepository.findByUser(getUser(email));
    }

    @Transactional
    public CartItem add(String email, CartRequest request) {
        User user = getUser(email);
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        CartItem item = cartRepository.findByUserAndProduct(user, product)
                .orElse(new CartItem(user, product, 0));

        int newQuantity = item.getQuantity() + request.quantity();
        if (newQuantity > product.getStock()) {
            throw new IllegalArgumentException("Not enough stock");
        }

        item.setQuantity(newQuantity);
        return cartRepository.save(item);
    }

    @Transactional
    public CartItem update(String email, Long itemId, UpdateCartRequest request) {
        User user = getUser(email);
        CartItem item = cartRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Cart item does not belong to this user");
        }
        if (request.quantity() > item.getProduct().getStock()) {
            throw new IllegalArgumentException("Not enough stock");
        }

        item.setQuantity(request.quantity());
        return cartRepository.save(item);
    }

    @Transactional
    public void remove(String email, Long itemId) {
        User user = getUser(email);
        CartItem item = cartRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Cart item does not belong to this user");
        }
        cartRepository.delete(item);
    }

    @Transactional
    public void clear(String email) {
        cartRepository.deleteByUser(getUser(email));
    }

    public BigDecimal total(String email) {
        return getCart(email).stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
