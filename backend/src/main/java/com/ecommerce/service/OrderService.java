package com.ecommerce.service;

import com.ecommerce.dto.request.OrderItemRequest;
import com.ecommerce.dto.request.OrderRequest;
import com.ecommerce.dto.request.OrderStatusRequest;
import com.ecommerce.dto.response.OrderItemResponse;
import com.ecommerce.dto.response.OrderResponse;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.exception.UnauthorizedException;
import com.ecommerce.model.*;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponse create(OrderRequest request, User currentUser) {
        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Produto não encontrado com id: " + itemRequest.getProductId()));

            if (!product.isActive()) {
                throw new ResourceNotFoundException("Produto não disponível: " + product.getName());
            }

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new InsufficientStockException(
                        product.getName(), product.getStock(), itemRequest.getQuantity());
            }

            // Registra preço histórico no momento da compra
            BigDecimal unitPrice = product.getPrice();
            total = total.add(unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity())));

            // Decrementa estoque
            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            items.add(OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(unitPrice)
                    .build());
        }

        Order order = Order.builder()
                .user(currentUser)
                .totalAmount(total)
                .build();

        // Vincula os itens ao pedido após o pedido ser construído
        for (OrderItem item : items) {
            item.setOrder(order);
            order.getItems().add(item);
        }

        return toResponse(orderRepository.save(order));
    }

    public List<OrderResponse> findByUser(User currentUser) {
        return orderRepository.findByUserOrderByCreatedAtDesc(currentUser).stream()
                .map(this::toResponse)
                .toList();
    }

    public OrderResponse findById(UUID id, User currentUser) {
        Order order = getOrThrow(id);
        boolean isOwner = order.getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new UnauthorizedException("Acesso negado ao pedido " + id);
        }
        return toResponse(order);
    }

    @Transactional
    public OrderResponse updateStatus(UUID id, OrderStatusRequest request) {
        Order order = getOrThrow(id);
        order.setStatus(request.getStatus());
        return toResponse(orderRepository.save(order));
    }

    private Order getOrThrow(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com id: " + id));
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .build())
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .items(itemResponses)
                .createdAt(order.getCreatedAt())
                .build();
    }
}
