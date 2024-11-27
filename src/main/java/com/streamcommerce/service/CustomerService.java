package com.streamcommerce.service;

import com.streamcommerce.dto.CustomerDTO;
import com.streamcommerce.dto.response.CartResponseDTO;

public interface CustomerService {

    CustomerDTO getCustomer();

    CartResponseDTO getCart();
}
