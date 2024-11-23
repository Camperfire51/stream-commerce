package com.streamcommerce.service;

import com.streamcommerce.dto.ProductDTO;
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
    public boolean doesProductExist(Long productId){
        return productRepository.existsById(productId);
    }

    @Override
    public List<ProductDTO> getProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, String categoryPath, Long vendorId, ProductStatus status) {
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
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDTO(product);
    }

    @Override
    public void submitProduct(ProductDTO productDTO){
        Product product = mapToEntity(productDTO);
        product.setDiscountPercentage(0);
        product.setStatus(ProductStatus.PENDING);
        productRepository.save(product);
    }

    @Override
    public void modifyProduct(ProductDTO productDTO){
        Product product = productRepository.findById(productDTO.getId()).orElseThrow();

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCategory(categoryRepository.findById(productDTO.getCategoryId()).orElseThrow());
        product.setVendor(vendorRepository.findById(productDTO.getVendorId()).orElseThrow());
        product.setStatus(ProductStatus.PENDING);

        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void setProductStatus(Long productId, ProductStatus status) {
        Product product = productRepository.findById(productId).orElseThrow();
        product.setStatus(status);
        productRepository.save(product);
    }

    @Override
    public boolean isProductAvailable(Long productId){
        Product product = productRepository.findById(productId).orElseThrow();

        if (product.getStatus() == ProductStatus.PUBLISHED && product.getQuantity() > 0) {

        }

        return product.getStatus() == ProductStatus.PUBLISHED;
    }

    private ProductDTO mapToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .build();
    }

    private Product mapToEntity(ProductDTO productDTO){
        return Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .category(categoryRepository.findById(productDTO.getCategoryId()).orElseThrow())
                .vendor(vendorRepository.findById(productDTO.getVendorId()).orElseThrow())
                .build();
    }
}
