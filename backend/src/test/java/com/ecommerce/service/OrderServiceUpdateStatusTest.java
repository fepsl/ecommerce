package com.ecommerce.service;

import com.ecommerce.dto.request.OrderStatusRequest;
import com.ecommerce.dto.response.OrderResponse;
import com.ecommerce.exception.InvalidStatusTransitionException;
import com.ecommerce.model.*;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService — updateStatus")
class OrderServiceUpdateStatusTest {

    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private Order orderWithStatus(OrderStatus status) {
        return Order.builder()
                .id(UUID.randomUUID())
                .user(User.builder().id(UUID.randomUUID()).role(Role.USER).build())
                .totalAmount(new BigDecimal("99.90"))
                .status(status)
                .build();
    }

    @ParameterizedTest(name = "{0} → {1}")
    @CsvSource({
        "PENDING,   PAID",
        "PENDING,   CANCELLED",
        "PAID,      SHIPPED",
        "PAID,      CANCELLED",
        "SHIPPED,   DELIVERED",
        "SHIPPED,   CANCELLED"
    })
    @DisplayName("transições válidas são aceitas")
    void updateStatus_validTransitions_succeed(OrderStatus current, OrderStatus next) {
        Order order = orderWithStatus(current);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenAnswer(inv -> (Order) inv.getArgument(0));

        OrderStatusRequest req = new OrderStatusRequest();
        req.setStatus(next);

        OrderResponse response = orderService.updateStatus(order.getId(), req);

        assertThat(response.getStatus()).isEqualTo(next);
        verify(orderRepository).save(order);
    }

    @ParameterizedTest(name = "{0} → {1}")
    @CsvSource({
        "PENDING,   SHIPPED",
        "PENDING,   DELIVERED",
        "PAID,      PENDING",
        "SHIPPED,   PENDING",
        "DELIVERED, PAID",
        "DELIVERED, CANCELLED",
        "CANCELLED, PAID",
        "CANCELLED, PENDING"
    })
    @DisplayName("transições inválidas lançam InvalidStatusTransitionException")
    void updateStatus_invalidTransitions_throwsException(OrderStatus current, OrderStatus next) {
        Order order = orderWithStatus(current);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        OrderStatusRequest req = new OrderStatusRequest();
        req.setStatus(next);

        assertThatThrownBy(() -> orderService.updateStatus(order.getId(), req))
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessageContaining(current.name())
                .hasMessageContaining(next.name());

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("DELIVERED não aceita nenhuma transição")
    void updateStatus_fromDelivered_alwaysThrows() {
        Order order = orderWithStatus(OrderStatus.DELIVERED);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.ofNullable(order));

        for (OrderStatus next : OrderStatus.values()) {
            OrderStatusRequest req = new OrderStatusRequest();
            req.setStatus(next);
            assertThatThrownBy(() -> orderService.updateStatus(order.getId(), req))
                    .isInstanceOf(InvalidStatusTransitionException.class);
        }
    }

    @Test
    @DisplayName("CANCELLED não aceita nenhuma transição")
    void updateStatus_fromCancelled_alwaysThrows() {
        Order order = orderWithStatus(OrderStatus.CANCELLED);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.ofNullable(order));

        for (OrderStatus next : OrderStatus.values()) {
            OrderStatusRequest req = new OrderStatusRequest();
            req.setStatus(next);
            assertThatThrownBy(() -> orderService.updateStatus(order.getId(), req))
                    .isInstanceOf(InvalidStatusTransitionException.class);
        }
    }
}
