package com.ecommerce.exception;

import com.ecommerce.model.OrderStatus;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(OrderStatus current, OrderStatus next) {
        super("Transição de status inválida: %s → %s".formatted(current, next));
    }
}
