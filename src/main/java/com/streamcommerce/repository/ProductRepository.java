package com.streamcommerce.repository;

import com.streamcommerce.model.Product;
import com.streamcommerce.model.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findAllByStatus(ProductStatus status); // Derived query method
}
