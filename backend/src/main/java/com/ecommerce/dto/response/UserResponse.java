package com.ecommerce.dto.response;

import com.ecommerce.model.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
    private AddressResponse address;
}
