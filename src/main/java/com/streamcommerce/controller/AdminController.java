package com.streamcommerce.controller;

import com.streamcommerce.dto.ProductDTO;
import com.streamcommerce.model.ProductStatus;
import com.streamcommerce.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ProductServiceImpl productService;

    @Autowired
    public AdminController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getProducts(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "vendorId", required = false) Long vendorId,
            @RequestParam(value = "status", required = false) ProductStatus status) {

        List<ProductDTO> products = productService.getProducts(name, minPrice, maxPrice, category, vendorId, status);

        return ResponseEntity.ok(products);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> setProductStatus(
            @RequestBody Long productId,
            @RequestParam(value = "status") ProductStatus status) {

        productService.setProductStatus(productId, status);
        return ResponseEntity.ok(ProductDTO.builder().build());
    }
}
