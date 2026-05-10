package com.ecommerce.service;

import com.ecommerce.dto.request.OrderItemRequest;
import com.ecommerce.dto.request.OrderRequest;
import com.ecommerce.dto.response.OrderResponse;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.UnauthorizedException;
import com.ecommerce.model.*;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService")
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private User buyer;
    private Product product;

    @BeforeEach
    void setUp() {
        buyer = User.builder()
                .id(UUID.randomUUID())
                .name("Comprador")
                .email("comprador@email.com")
                .role(Role.USER)
                .build();

        product = Product.builder()
                .id(UUID.randomUUID())
                .name("Camiseta")
                .price(new BigDecimal("99.90"))
                .stock(10)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("create lança InsufficientStockException quando quantidade > estoque")
    void create_insufficientStock_throwsException() {
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(product.getId());
        itemRequest.setQuantity(20); // mais que stock=10

        OrderRequest request = new OrderRequest();
        request.setItems(List.of(itemRequest));

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> orderService.create(request, buyer))
                .isInstanceOf(InsufficientStockException.class);

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("create salva unit_price igual ao preço atual do produto")
    void create_savesUnitPriceAsCurrentProductPrice() {
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(product.getId());
        itemRequest.setQuantity(2);

        OrderRequest request = new OrderRequest();
        request.setItems(List.of(itemRequest));

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderResponse response = orderService.create(request, buyer);

        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().get(0).getUnitPrice())
                .isEqualByComparingTo(new BigDecimal("99.90"));
        assertThat(response.getTotalAmount())
                .isEqualByComparingTo(new BigDecimal("199.80")); // 99.90 * 2
    }

    @Test
    @DisplayName("create decrementa o estoque do produto ao criar pedido")
    void create_decrementsProductStock() {
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(product.getId());
        itemRequest.setQuantity(3);

        OrderRequest request = new OrderRequest();
        request.setItems(List.of(itemRequest));

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        orderService.create(request, buyer);

        assertThat(product.getStock()).isEqualTo(7); // 10 - 3
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("findByUser retorna apenas pedidos do usuário informado")
    void findByUser_returnsOnlyUserOrders() {
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .user(buyer)
                .totalAmount(new BigDecimal("99.90"))
                .build();

        when(orderRepository.findByUserOrderByCreatedAtDesc(buyer)).thenReturn(List.of(order));

        List<OrderResponse> result = orderService.findByUser(buyer);

        assertThat(result).hasSize(1);
        verify(orderRepository).findByUserOrderByCreatedAtDesc(buyer);
        verify(orderRepository, never()).findByUserOrderByCreatedAtDesc(argThat(u -> !u.equals(buyer)));
    }

    @Test
    @DisplayName("findById lança UnauthorizedException quando USER tenta ver pedido de outro usuário")
    void findById_otherUsersOrder_throwsUnauthorized() {
        User otherUser = User.builder()
                .id(UUID.randomUUID())
                .role(Role.USER)
                .build();

        Order order = Order.builder()
                .id(UUID.randomUUID())
                .user(otherUser)
                .totalAmount(new BigDecimal("99.90"))
                .build();

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.findById(order.getId(), buyer))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    @DisplayName("findById permite ADMIN ver pedido de qualquer usuário")
    void findById_adminAccessingOtherOrder_succeeds() {
        User admin = User.builder()
                .id(UUID.randomUUID())
                .role(Role.ADMIN)
                .build();

        Order order = Order.builder()
                .id(UUID.randomUUID())
                .user(buyer)
                .totalAmount(new BigDecimal("99.90"))
                .build();

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThatNoException().isThrownBy(() -> orderService.findById(order.getId(), admin));
    }
}
