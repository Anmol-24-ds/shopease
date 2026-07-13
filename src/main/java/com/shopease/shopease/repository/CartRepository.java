package com.shopease.shopease.repository;

import com.shopease.shopease.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
