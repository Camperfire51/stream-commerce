package com.streamcommerce.dto;

import com.streamcommerce.model.Cart;
import com.streamcommerce.model.Order;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CustomerDTO extends UserDTO {
    private Set<Order> orderHistory = new HashSet<>();
    private Cart cart = new Cart();
}
