package com.streamcommerce.controller;

import com.streamcommerce.dto.ProductDTO;
import com.streamcommerce.model.ProductStatus;
import com.streamcommerce.service.AuthenticationService;
import com.streamcommerce.service.AuthenticationServiceImpl;
import com.streamcommerce.service.ProductService;
import com.streamcommerce.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    private final ProductService productService;
    private final AuthenticationService authenticationService;

    @Autowired
    public VendorController(ProductServiceImpl productService, AuthenticationServiceImpl authenticationService) {
        this.productService = productService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getProducts(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "vendorId", required = false) Long vendorId,
            @RequestParam(value = "status", required = false) ProductStatus status) {

        Long authenticatedVendorId = authenticationService.getAuthenticatedUserId();

        if (!ProductStatus.PUBLISHED.equals(status) && !authenticatedVendorId.equals(vendorId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(List.of());
        }

        List<ProductDTO> products = productService.getProducts(name, minPrice, maxPrice, category, authenticatedVendorId, ProductStatus.PUBLISHED);

        return ResponseEntity.ok(products);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> submitProduct(@RequestBody ProductDTO productDTO) {
        Long authenticatedVendorId = authenticationService.getAuthenticatedUserId();

        if (!authenticatedVendorId.equals(productDTO.getVendorId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(productDTO);

        productService.submitProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    @PutMapping("/products")
    public ResponseEntity<ProductDTO> modifyProduct(@RequestBody ProductDTO newProductDTO) {
        Long authenticatedVendorId = authenticationService.getAuthenticatedUserId();
        Long productId = newProductDTO.getId();

        if (productService.doesProductExist(productId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ProductDTO.builder().build());

        if (!authenticatedVendorId.equals(productId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ProductDTO.builder().build());

        productService.modifyProduct(newProductDTO);
        return ResponseEntity.status(HttpStatus.OK).body(newProductDTO);
    }

    @DeleteMapping("/products")
    public ResponseEntity<ProductDTO> deleteProduct(@RequestParam(value = "productId") Long productId) {
        Long authenticatedVendorId = authenticationService.getAuthenticatedUserId();

        if (productService.doesProductExist(productId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ProductDTO.builder().build());

        if (!authenticatedVendorId.equals(productId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ProductDTO.builder().build());

        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(ProductDTO.builder().build());
    }

}
