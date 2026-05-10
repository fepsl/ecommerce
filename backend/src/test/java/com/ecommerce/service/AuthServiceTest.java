package com.ecommerce.service;

import com.ecommerce.dto.request.LoginRequest;
import com.ecommerce.dto.request.RegisterRequest;
import com.ecommerce.dto.response.AuthResponse;
import com.ecommerce.exception.EmailAlreadyExistsException;
import com.ecommerce.model.Role;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.security.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService")
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("register com email novo retorna AuthResponse com token")
    void register_newEmail_returnsAuthResponseWithToken() {
        RegisterRequest request = new RegisterRequest();
        request.setName("João Silva");
        request.setEmail("joao@email.com");
        request.setPassword("senha123");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtUtil.generateToken(any(User.class))).thenReturn("jwt-token-fake");

        AuthResponse response = authService.register(request);

        assertThat(response.getToken()).isEqualTo("jwt-token-fake");
        assertThat(response.getEmail()).isEqualTo("joao@email.com");
        assertThat(response.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("register com email duplicado lança EmailAlreadyExistsException")
    void register_duplicateEmail_throwsException() {
        RegisterRequest request = new RegisterRequest();
        request.setName("João");
        request.setEmail("existente@email.com");
        request.setPassword("senha123");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("register codifica a senha com BCrypt antes de salvar")
    void register_encodesPasswordBeforeSaving() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Maria");
        request.setEmail("maria@email.com");
        request.setPassword("senha-pura");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode("senha-pura")).thenReturn("$2a$hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtUtil.generateToken(any())).thenReturn("token");

        authService.register(request);

        verify(passwordEncoder).encode("senha-pura");
        verify(userRepository).save(argThat(u -> u.getPassword().equals("$2a$hashed")));
    }

    @Test
    @DisplayName("login com credenciais válidas retorna AuthResponse com token")
    void login_validCredentials_returnsAuthResponseWithToken() {
        LoginRequest request = new LoginRequest();
        request.setEmail("joao@email.com");
        request.setPassword("senha123");

        User user = User.builder()
                .id(UUID.randomUUID())
                .name("João")
                .email("joao@email.com")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("jwt-valido");

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("jwt-valido");
        assertThat(response.getEmail()).isEqualTo("joao@email.com");
    }

    @Test
    @DisplayName("login com senha errada lança BadCredentialsException")
    void login_wrongPassword_throwsException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("joao@email.com");
        request.setPassword("senha-errada");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);

        verify(userRepository, never()).findByEmail(anyString());
    }
}
