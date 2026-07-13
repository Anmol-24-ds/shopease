package com.shopease.shopease.repository;
import com.shopease.shopease.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product,Long> {
}
