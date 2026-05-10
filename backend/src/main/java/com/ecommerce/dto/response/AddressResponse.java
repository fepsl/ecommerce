package com.ecommerce.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AddressResponse {
    private UUID id;
    private String street;
    private String number;
    private String city;
    private String state;
    private String zip;
}
