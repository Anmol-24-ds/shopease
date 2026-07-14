package com.shopease.shopease.controller;
import com.shopease.shopease.model.Order;
import com.shopease.shopease.model.User;
import com.shopease.shopease.service.OrderService;
import com.shopease.shopease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/place")
    public Order placeOrder(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        return orderService.placeOrder(user);
    }

    @GetMapping("/my")
    public List<Order> getMyOrders(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        return orderService.getOrdersByUser(user);
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{orderId}/status")
    public Order updateStatus(@PathVariable Long orderId, @RequestParam String status) {
        return orderService.updateOrderStatus(orderId, status);
    }
}