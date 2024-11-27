package com.streamcommerce.service;

import com.streamcommerce.dto.CustomerDTO;
import com.streamcommerce.dto.response.CartResponseDTO;
import com.streamcommerce.model.Cart;
import com.streamcommerce.model.Customer;
import com.streamcommerce.model.UserPrincipal;
import com.streamcommerce.repository.CustomerRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO getCustomer() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long customerId = userPrincipal.getId();

        Customer customer = customerRepository.findById(customerId).orElseThrow();
        return mapToDTO(customer);
    }

    @Override
    public CartResponseDTO getCart() {
        Cart cart = getCustomer().getCart();
        return mapToDTO
    }

    private CustomerDTO mapToDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .username(customer.get())
                .email(customer.getEmail())
                .createdDate(customer.getCreatedDate())
                .address(customer.getAddress())
                .userType(customer.getUserType())
                .userStatus(customer.getUserStatus())
                .orderHistory(customer.getOrderHistory())
                .cart(customer.getCart())
                .build();
    }
}
