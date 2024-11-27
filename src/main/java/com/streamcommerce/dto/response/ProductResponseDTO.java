package com.streamcommerce.dto.response;

import com.streamcommerce.model.ProductStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {
    private Long id;
    private String name;
    private BigDecimal basePrice;
    private BigDecimal discountPercentage;
    private BigDecimal finalPrice;
    private BigDecimal discountAmount;
    private String description;
    private Long categoryId;
    private Long vendorId;
    private ProductStatus status;
}
