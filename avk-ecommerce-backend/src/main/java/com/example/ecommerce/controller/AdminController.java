package com.example.ecommerce.controller;

import com.example.ecommerce.dto.UpdateOrderStatusRequest;
import com.example.ecommerce.entity.CustomerOrder;
import com.example.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final OrderService orderService;

    public AdminController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public List<CustomerOrder> allOrders() {
        return orderService.allOrders();
    }

    @PutMapping("/orders/{id}/status")
    public CustomerOrder updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        return orderService.updateStatus(id, request.status());
    }
}
