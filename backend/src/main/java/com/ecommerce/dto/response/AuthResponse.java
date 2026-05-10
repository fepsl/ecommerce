package com.ecommerce.dto.response;

import com.ecommerce.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String name;
    private String email;
    private Role role;
}
