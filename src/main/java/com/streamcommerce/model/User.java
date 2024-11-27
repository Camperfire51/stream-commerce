package com.streamcommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String email;

    protected LocalDateTime createdDate;

    @Embedded
    protected Address address;

    @Enumerated(EnumType.STRING)
    protected UserType userType;

    @Enumerated(EnumType.STRING)
    protected UserStatus userStatus;
}
