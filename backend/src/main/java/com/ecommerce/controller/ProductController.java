package com.ecommerce.controller;

import com.ecommerce.dto.request.ProductRequest;
import com.ecommerce.dto.response.ProductResponse;
import com.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Produtos")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Listar produtos com filtros e paginação")
    public Page<ProductResponse> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 12) Pageable pageable) {
        return productService.findAll(name, category, minPrice, maxPrice, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ProductResponse findById(@PathVariable UUID id) {
        return productService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar produto", security = @SecurityRequirement(name = "Bearer Token"))
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar produto", security = @SecurityRequirement(name = "Bearer Token"))
    public ProductResponse update(@PathVariable UUID id, @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desativar produto (soft delete)", security = @SecurityRequirement(name = "Bearer Token"))
    public void deactivate(@PathVariable UUID id) {
        productService.deactivate(id);
    }
}
