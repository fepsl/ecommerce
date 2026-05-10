package com.ecommerce.repository;

import com.ecommerce.model.Address;
import com.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    Optional<Address> findByUser(User user);
}
