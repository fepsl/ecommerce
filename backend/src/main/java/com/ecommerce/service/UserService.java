package com.ecommerce.service;

import com.ecommerce.dto.request.AddressRequest;
import com.ecommerce.dto.response.AddressResponse;
import com.ecommerce.dto.response.UserResponse;
import com.ecommerce.model.Address;
import com.ecommerce.model.User;
import com.ecommerce.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AddressRepository addressRepository;

    public UserResponse getProfile(User user) {
        Address address = addressRepository.findByUser(user).orElse(null);

        AddressResponse addressResponse = address == null ? null : AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .number(address.getNumber())
                .city(address.getCity())
                .state(address.getState())
                .zip(address.getZip())
                .build();

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .address(addressResponse)
                .build();
    }

    public AddressResponse updateAddress(User user, AddressRequest request) {
        Address address = addressRepository.findByUser(user)
                .orElse(Address.builder().user(user).build());

        address.setStreet(request.getStreet());
        address.setNumber(request.getNumber());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZip(request.getZip());

        Address saved = addressRepository.save(address);

        return AddressResponse.builder()
                .id(saved.getId())
                .street(saved.getStreet())
                .number(saved.getNumber())
                .city(saved.getCity())
                .state(saved.getState())
                .zip(saved.getZip())
                .build();
    }
}
