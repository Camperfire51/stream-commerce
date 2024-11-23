package com.streamcommerce.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.collection.spi.PersistentSet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer; // The customer who placed the order

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<Product, Integer> products = new HashMap<>(); // Map of product and quantity

    private LocalDateTime orderDate; // The date and time the order was placed

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // The status of the order (e.g., PENDING, COMPLETED, CANCELED)

    private Address shippingAddress; // Address for the order's delivery

    public BigDecimal getOrderTotal() {
        return products.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(new BigDecimal(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
