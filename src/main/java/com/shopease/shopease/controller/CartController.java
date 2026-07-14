package com.shopease.shopease.controller;
import com.shopease.shopease.model.Cart;
import com.shopease.shopease.model.User;
import com.shopease.shopease.service.CartService;
import com.shopease.shopease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public Cart addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam Integer quantity) {
        User user = userService.getUserById(userId);
        return cartService.addItemToCart(user, productId, quantity);
    }

    @GetMapping
    public Cart getCart(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        return cartService.getCartByUser(user);
    }

    @DeleteMapping("/item/{cartItemId}")
    public void removeItem(@PathVariable Long cartItemId) {
        cartService.removeItemFromCart(cartItemId);
    }
}