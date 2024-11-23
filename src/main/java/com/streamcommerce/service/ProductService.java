package com.streamcommerce.service;

import com.streamcommerce.dto.ProductDTO;
import com.streamcommerce.model.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    boolean doesProductExist(Long productId);

    List<ProductDTO> getProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, String categoryPath, Long vendorId, ProductStatus status);

    ProductDTO getProductById(Long productId);

    void submitProduct(ProductDTO productDTO);

    void modifyProduct(ProductDTO productDTO);

    void deleteProduct(Long id);

    void setProductStatus(Long productId, ProductStatus status);

    boolean isProductAvailable(Long productId);
}
