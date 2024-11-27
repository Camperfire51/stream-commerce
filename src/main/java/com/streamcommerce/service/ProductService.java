package com.streamcommerce.service;

import com.streamcommerce.dto.request.ProductRequestDTO;
import com.streamcommerce.dto.response.ProductResponseDTO;
import com.streamcommerce.model.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductResponseDTO getProduct(Long productId);

    List<ProductResponseDTO> getProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, String categoryPath, Long vendorId, ProductStatus status);

    void submitProduct(ProductRequestDTO productRequestDTO);

    void modifyProduct(Long productId, ProductRequestDTO productRequestDTO);

    void deleteProduct(Long id);

    void modifyProductStatus(Long id, ProductStatus status);
}
