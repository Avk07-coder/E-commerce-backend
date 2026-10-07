package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CheckoutRequest;
import com.example.ecommerce.entity.CustomerOrder;
import com.example.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/checkout")
    public CustomerOrder checkout(Principal principal, @Valid @RequestBody CheckoutRequest request) {
        return service.checkout(principal.getName(), request);
    }

    @GetMapping
    public List<CustomerOrder> myOrders(Principal principal) {
        return service.myOrders(principal.getName());
    }

    @GetMapping("/{id}")
    public CustomerOrder getOrder(Principal principal, @PathVariable Long id) {
        return service.getOrder(principal.getName(), id);
    }

    @PostMapping("/{id}/cancel")
    public Map<String, String> cancel(Principal principal, @PathVariable Long id) {
        CustomerOrder order = service.getOrder(principal.getName(), id);
        if (order.getStatus().name().equals("DELIVERED")) {
            throw new IllegalArgumentException("Delivered order cannot be cancelled");
        }
        service.updateStatus(id, com.example.ecommerce.entity.OrderStatus.CANCELLED);
        return Map.of("message", "Order cancelled");
    }
}
