package com.ecommerce.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @NotEmpty(message = "Pedido deve ter pelo menos um item")
    @Valid
    private List<OrderItemRequest> items;
}
