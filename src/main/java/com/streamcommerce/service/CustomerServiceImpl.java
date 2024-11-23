package com.streamcommerce.service;

import com.streamcommerce.dto.CustomerDTO;
import com.streamcommerce.model.Customer;
import com.streamcommerce.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        return mapToDTO(customer);
    }

    private CustomerDTO mapToDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .username(customer.getUsername())
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
