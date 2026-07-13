package com.shopease.shopease.service;
import com.shopease.shopease.model.Cart;
import com.shopease.shopease.model.CartItem;
import com.shopease.shopease.model.Product;
import com.shopease.shopease.model.User;
import com.shopease.shopease.repository.CartRepository;
import com.shopease.shopease.repository.CartItemRepository;
import com.shopease.shopease.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart getOrCreateCart(User user) {
        return cartRepository.findAll().stream()
                .filter(cart -> cart.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    public Cart addItemToCart(User user, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available for: " + product.getName());
        }

        Cart cart = getOrCreateCart(user);

        CartItem existingItem = cart.getItems() == null ? null :
                cart.getItems().stream()
                        .filter(item -> item.getProduct().getId().equals(productId))
                        .findFirst()
                        .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cartItemRepository.save(newItem);
        }

        return cartRepository.findById(cart.getId()).get();
    }

    public void removeItemFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public Cart getCartByUser(User user) {
        return getOrCreateCart(user);
    }
}