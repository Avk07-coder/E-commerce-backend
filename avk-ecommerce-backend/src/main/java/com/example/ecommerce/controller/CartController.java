package com.example.ecommerce.controller;

import com.example.ecommerce.dto.*;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping
    public List<CartItem> getCart(Principal principal) {
        return service.getCart(principal.getName());
    }

    @GetMapping("/total")
    public Map<String, BigDecimal> total(Principal principal) {
        return Map.of("total", service.total(principal.getName()));
    }

    @PostMapping
    public CartItem add(Principal principal, @Valid @RequestBody CartRequest request) {
        return service.add(principal.getName(), request);
    }

    @PutMapping("/{itemId}")
    public CartItem update(Principal principal, @PathVariable Long itemId,
                           @Valid @RequestBody UpdateCartRequest request) {
        return service.update(principal.getName(), itemId, request);
    }

    @DeleteMapping("/{itemId}")
    public Map<String, String> remove(Principal principal, @PathVariable Long itemId) {
        service.remove(principal.getName(), itemId);
        return Map.of("message", "Item removed");
    }

    @DeleteMapping
    public Map<String, String> clear(Principal principal) {
        service.clear(principal.getName());
        return Map.of("message", "Cart cleared");
    }
}
