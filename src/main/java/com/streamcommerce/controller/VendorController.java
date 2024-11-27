package com.streamcommerce.controller;

import com.streamcommerce.dto.request.ProductRequestDTO;
import com.streamcommerce.dto.response.ProductResponseDTO;
import com.streamcommerce.model.Product;
import com.streamcommerce.model.ProductStatus;
import com.streamcommerce.model.UserPrincipal;
import com.streamcommerce.service.AuthUserDetailsService;
import com.streamcommerce.service.ProductService;
import com.streamcommerce.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    private final ProductService productService;
    private final AuthUserDetailsService authUserDetailsService;

    @Autowired
    public VendorController(ProductServiceImpl productService, AuthUserDetailsService authUserDetailsService) {
        this.productService = productService;
        this.authUserDetailsService = authUserDetailsService;
    }

    @GetMapping("/product")
    public ResponseEntity<ProductResponseDTO> getProductById(@RequestParam(value = "id") Long id) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId = userPrincipal.getId();

        ProductResponseDTO product = productService.getProduct(id);

        if (!ProductStatus.PUBLISHED.equals(product.getStatus()) && !userId.equals(product.getVendorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ProductResponseDTO.builder().build());
        }

        return ResponseEntity.ok(product);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getProductsWithFilters(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "vendorId", required = false) Long vendorId,
            @RequestParam(value = "status", required = false) ProductStatus status) {

        if (!ProductStatus.PUBLISHED.equals(status) && !authenticatedVendorId.equals(vendorId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(List.of());
        }

        List<ProductResponseDTO> products = productService.getProducts(name, minPrice, maxPrice, category, authenticatedVendorId, ProductStatus.PUBLISHED);

        return ResponseEntity.ok(products);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDTO> submitProduct(
            @RequestParam(value = "productId") Long productId,
            @RequestBody ProductRequestDTO productRequestDTO) {
        Long authenticatedVendorId = authenticationService.getAuthenticatedUserId();

        if (!authenticatedVendorId.equals(productDTO.getVendorId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(productDTO);

        productService.submitProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    @PutMapping("/products")
    public ResponseEntity<ProductRequestDTO> modifyProduct(
            @RequestParam(value = "productId") Long productId,
            @RequestBody ProductRequestDTO productRequestDTO) {
        Long authenticatedVendorId = authenticationService.getAuthenticatedUserId();

        if (productService.doesProductExist(productId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ProductDTO.builder().build());

        if (!authenticatedVendorId.equals(productId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ProductDTO.builder().build());

        productService.modifyProduct(newProductDTO);
        return ResponseEntity.status(HttpStatus.OK).body(newProductDTO);
    }

    @DeleteMapping("/products")
    public ResponseEntity<ProductRequestDTO> deleteProduct(@RequestParam(value = "productId") Long productId) {
        Long authenticatedVendorId = authenticationService.getAuthenticatedUserId();

        if (productService.doesProductExist(productId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ProductRequestDTO.builder().build());

        if (!authenticatedVendorId.equals(productId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ProductRequestDTO.builder().build());

        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(ProductRequestDTO.builder().build());
    }

}
