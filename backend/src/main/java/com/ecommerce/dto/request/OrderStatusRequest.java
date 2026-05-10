package com.ecommerce.dto.request;

import com.ecommerce.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusRequest {

    @NotNull(message = "Status é obrigatório")
    private OrderStatus status;
}
