package com.ecommerce.controller;

import com.ecommerce.dto.request.AddressRequest;
import com.ecommerce.dto.response.AddressResponse;
import com.ecommerce.dto.response.UserResponse;
import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuário")
@SecurityRequirement(name = "Bearer Token")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Perfil do usuário autenticado")
    public UserResponse getProfile(@AuthenticationPrincipal User currentUser) {
        return userService.getProfile(currentUser);
    }

    @PutMapping("/me/address")
    @Operation(summary = "Atualizar endereço")
    public AddressResponse updateAddress(@AuthenticationPrincipal User currentUser,
                                         @Valid @RequestBody AddressRequest request) {
        return userService.updateAddress(currentUser, request);
    }
}
