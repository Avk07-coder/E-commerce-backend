package com.example.ecommerce.service;

import com.example.ecommerce.dto.CheckoutRequest;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final CartItemRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(UserRepository userRepository, CartItemRepository cartRepository,
                        OrderRepository orderRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public CustomerOrder checkout(String email, CheckoutRequest request) {
        User user = getUser(email);
        List<CartItem> cart = cartRepository.findByUser(user);

        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        CustomerOrder order = new CustomerOrder();
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());
        order.setShippingAddress(request.shippingAddress());

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart) {
            Product product = cartItem.getProduct();

            if (cartItem.getQuantity() > product.getStock()) {
                throw new IllegalArgumentException("Insufficient stock for " + product.getName());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    product.getPrice()
            );

            order.getItems().add(orderItem);
            total = total.add(product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setTotalAmount(total);
        CustomerOrder saved = orderRepository.save(order);
        cartRepository.deleteByUser(user);

        return saved;
    }

    public List<CustomerOrder> myOrders(String email) {
        return orderRepository.findByUserOrderByCreatedAtDesc(getUser(email));
    }

    public CustomerOrder getOrder(String email, Long orderId) {
        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new IllegalArgumentException("Order does not belong to this user");
        }
        return order;
    }

    public List<CustomerOrder> allOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public CustomerOrder updateStatus(Long orderId, OrderStatus status) {
        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
