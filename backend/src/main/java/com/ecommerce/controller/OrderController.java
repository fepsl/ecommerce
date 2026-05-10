package com.ecommerce.controller;

import com.ecommerce.dto.request.OrderRequest;
import com.ecommerce.dto.request.OrderStatusRequest;
import com.ecommerce.dto.response.OrderResponse;
import com.ecommerce.model.User;
import com.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Pedidos")
@SecurityRequirement(name = "Bearer Token")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar pedido")
    public OrderResponse create(@Valid @RequestBody OrderRequest request,
                                @AuthenticationPrincipal User currentUser) {
        return orderService.create(request, currentUser);
    }

    @GetMapping("/me")
    @Operation(summary = "Meus pedidos")
    public List<OrderResponse> myOrders(@AuthenticationPrincipal User currentUser) {
        return orderService.findByUser(currentUser);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhe do pedido (dono ou admin)")
    public OrderResponse findById(@PathVariable UUID id,
                                  @AuthenticationPrincipal User currentUser) {
        return orderService.findById(id, currentUser);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar status do pedido")
    public OrderResponse updateStatus(@PathVariable UUID id,
                                      @Valid @RequestBody OrderStatusRequest request) {
        return orderService.updateStatus(id, request);
    }
}
