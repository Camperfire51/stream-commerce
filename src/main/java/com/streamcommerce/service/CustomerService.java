package com.streamcommerce.service;

import com.streamcommerce.dto.CustomerDTO;

public interface CustomerService {

    CustomerDTO getCustomerById(Long customerId);
}
