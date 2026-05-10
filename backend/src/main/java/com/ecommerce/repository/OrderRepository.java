package com.ecommerce.repository;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Order> findByUserOrderByCreatedAtDesc(User user);

    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<Order> findById(UUID id);
}
