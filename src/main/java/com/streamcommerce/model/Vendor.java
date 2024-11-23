package com.streamcommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor extends User{
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Set<Product> products;
}
