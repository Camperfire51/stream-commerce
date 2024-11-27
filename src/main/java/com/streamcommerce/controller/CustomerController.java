package com.streamcommerce.controller;

import com.streamcommerce.dto.CustomerDTO;
import com.streamcommerce.dto.response.ProductResponseDTO;
import com.streamcommerce.model.Cart;
import com.streamcommerce.model.Order;
import com.streamcommerce.model.ProductStatus;
import com.streamcommerce.model.UserPrincipal;
import com.streamcommerce.service.CustomerService;
import com.streamcommerce.service.ProductService;
import com.streamcommerce.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final ProductService productService;
    private final CustomerService customerService;

    @Autowired
    public CustomerController(ProductServiceImpl productService, CustomerService customerService) {
        this.productService = productService;
        this.customerService = customerService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getProducts(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "vendorId", required = false) Long vendorId) {

        List<ProductResponseDTO> products = productService.getProducts(name, minPrice, maxPrice, category, vendorId, ProductStatus.PUBLISHED);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/cart")
    public ResponseEntity<Cart> getCartContent() {



        ProductResponseDTO product = productService(id);

        Long userId = authenticationService.getAuthenticatedUserId();
        CustomerDTO customer = customerService.getCustomerById(userId);
        return ResponseEntity.ok(customer.getCart());
    }

    @PostMapping("/cart")
    public ResponseEntity<ProductDTO> addProductToCart(
            @RequestParam(value = "productId") Long productId,
            @RequestParam(value = "quantity") int quantity) {
        Long userId = authenticationService.getAuthenticatedUserId();
        CustomerDTO customer = customerService.getCustomerById(userId);


    }

    @DeleteMapping("/cart")
    public ResponseEntity<ProductDTO> removeProductFromCart(
            @RequestParam(value = "productId") Long productId,
            @RequestParam(value = "quantity") int quantity){


    }

    @PutMapping("/reset-cart")
    public ResponseEntity<ProductDTO> resetCart(){

    }

    @PostMapping("/order")
    public ResponseEntity<Order> order(){

    }
}
