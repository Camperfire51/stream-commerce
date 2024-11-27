package com.streamcommerce.service;

import com.streamcommerce.dto.request.ProductRequestDTO;
import com.streamcommerce.dto.response.ProductResponseDTO;
import com.streamcommerce.exception.ProductNotFoundException;
import com.streamcommerce.model.Product;
import com.streamcommerce.model.ProductStatus;
import com.streamcommerce.repository.CategoryRepository;
import com.streamcommerce.repository.ProductRepository;
import com.streamcommerce.repository.VendorRepository;
import com.streamcommerce.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public ProductResponseDTO getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponse(product);
    }

    @Override
    public List<ProductResponseDTO> getProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, String categoryPath, Long vendorId, ProductStatus status) {
        Specification<Product> spec = Specification.where(null); // Start with no specifications

        // Add name filter if provided
        if (name != null && !name.isEmpty()) {
            spec = spec.and(ProductSpecification.hasName(name));
        }

        // Add category path filter if provided
        if (categoryPath != null && !categoryPath.isEmpty()) {
            spec = spec.and(ProductSpecification.hasCategoryPath(categoryPath));
        }

        // Add minPrice filter if provided
        if (minPrice != null) {
            spec = spec.and(ProductSpecification.hasPriceGreaterThanOrEqual(minPrice));
        }

        // Add maxPrice filter if provided
        if (maxPrice != null) {
            spec = spec.and(ProductSpecification.hasPriceLessThanOrEqual(maxPrice));
        }

        // Add vendor filter if provided
        if (vendorId != null) {
            spec = spec.and(ProductSpecification.hasVendorById(vendorId));
        }

        if (status != null) {
            spec = spec.and(ProductSpecification.hasStatus(status));
        }

        return productRepository.findAll(spec).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void submitProduct(ProductRequestDTO productRequestDTO) {
        Product product = mapToEntity(productRequestDTO);
        // New products are always in "PENDING" status.
        product.setStatus(ProductStatus.PENDING);
        productRepository.save(product);
    }

    @Override
    public void modifyProduct(Long productId, ProductRequestDTO productRequestDTO) {
        if(!productRepository.existsById(productId))
            throw new ProductNotFoundException("Product with id (" + productId + ") was not found");

        Product product = mapToEntity(productRequestDTO);
        product.setId(productId);
        product.setStatus(ProductStatus.PENDING);

        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void modifyProductStatus(Long id, ProductStatus status) {

    }

    @Override
    public void setProductStatus(Long productId, ProductStatus status) {
        Product product = productRepository.findById(productId).orElseThrow();
        product.setStatus(status);
        productRepository.save(product);
    }

    @Override
    public boolean isProductAvailable(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();

        if (product.getStatus() == ProductStatus.PUBLISHED && product.getQuantity() > 0) {

        }

        return product.getStatus() == ProductStatus.PUBLISHED;
    }

    private Product mapToEntity(ProductRequestDTO productRequestDTO) {
        return Product.builder()
                .name(productRequestDTO.getName())
                .basePrice(productRequestDTO.getBasePrice())
                .discountPercentage(productRequestDTO.getDiscountPercentage())
                .description(productRequestDTO.getDescription())
                .category(categoryRepository.findById(productRequestDTO.getCategoryId()).orElseThrow())
                .vendor(vendorRepository.findById(productRequestDTO.getVendorId()).orElseThrow())
                .quantity(productRequestDTO.getQuantity())
                .build();
    }

    private ProductResponseDTO mapToResponse(Product product) {
        BigDecimal discountAmount = product.getBasePrice()
                .multiply(product.getDiscountPercentage())
                .divide(BigDecimal.valueOf(100)); // Discount percentage to decimal form

        BigDecimal finalPrice = product.getBasePrice().subtract(discountAmount);

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .basePrice(product.getBasePrice())
                .discountPercentage(product.getDiscountPercentage())
                .finalPrice(finalPrice)
                .discountAmount(discountAmount)
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .vendorId(product.getVendor().getId())
                .build();
    }
}
