package com.streamcommerce.dto.request;

import com.streamcommerce.model.Address;
import com.streamcommerce.model.UserType;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class UserRequestDTO {
    protected String username;
    protected String email;
    protected LocalDateTime createdDate;
    protected Address address;
    protected UserType userType;
}
