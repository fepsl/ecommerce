package com.ecommerce.service;

import com.ecommerce.dto.request.AddressRequest;
import com.ecommerce.dto.response.AddressResponse;
import com.ecommerce.dto.response.UserResponse;
import com.ecommerce.model.Address;
import com.ecommerce.model.Role;
import com.ecommerce.model.User;
import com.ecommerce.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService")
@SuppressWarnings("null")
class UserServiceTest {

    @Mock private AddressRepository addressRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .name("Fulano")
                .email("fulano@email.com")
                .role(Role.USER)
                .build();
    }

    @Test
    @DisplayName("getProfile retorna perfil com endereço quando existe")
    void getProfile_withAddress_returnsAddressInResponse() {
        Address address = Address.builder()
                .id(UUID.randomUUID())
                .user(user)
                .street("Rua A")
                .number("10")
                .city("SP")
                .state("SP")
                .zip("01310-100")
                .build();

        when(addressRepository.findByUser(user)).thenReturn(Optional.of(address));

        UserResponse response = userService.getProfile(user);

        assertThat(response.getAddress()).isNotNull();
        assertThat(response.getAddress().getStreet()).isEqualTo("Rua A");
        assertThat(response.getEmail()).isEqualTo("fulano@email.com");
    }

    @Test
    @DisplayName("getProfile retorna endereço nulo quando não existe")
    void getProfile_withoutAddress_returnsNullAddress() {
        when(addressRepository.findByUser(user)).thenReturn(Optional.empty());

        UserResponse response = userService.getProfile(user);

        assertThat(response.getAddress()).isNull();
    }

    @Test
    @DisplayName("updateAddress cria endereço quando usuário não tem nenhum (upsert)")
    void updateAddress_noExistingAddress_createsNew() {
        when(addressRepository.findByUser(user)).thenReturn(Optional.empty());
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> {
            Address saved = (Address) inv.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        AddressRequest request = buildRequest("Rua Nova", "42", "Campinas", "SP", "13010-000");

        AddressResponse response = userService.updateAddress(user, request);

        assertThat(response.getStreet()).isEqualTo("Rua Nova");
        assertThat(response.getCity()).isEqualTo("Campinas");
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    @DisplayName("updateAddress atualiza endereço existente sem criar novo registro")
    void updateAddress_existingAddress_updatesFields() {
        Address existing = Address.builder()
                .id(UUID.randomUUID())
                .user(user)
                .street("Rua Velha")
                .number("1")
                .city("Rio")
                .state("RJ")
                .zip("20040-020")
                .build();

        when(addressRepository.findByUser(user)).thenReturn(Optional.of(existing));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));

        AddressRequest request = buildRequest("Rua Nova", "99", "SP", "SP", "01310-100");

        AddressResponse response = userService.updateAddress(user, request);

        assertThat(response.getStreet()).isEqualTo("Rua Nova");
        assertThat(response.getNumber()).isEqualTo("99");
        verify(addressRepository, times(1)).save(existing);
    }

    private AddressRequest buildRequest(String street, String number, String city, String state, String zip) {
        AddressRequest req = new AddressRequest();
        req.setStreet(street);
        req.setNumber(number);
        req.setCity(city);
        req.setState(state);
        req.setZip(zip);
        return req;
    }
}
