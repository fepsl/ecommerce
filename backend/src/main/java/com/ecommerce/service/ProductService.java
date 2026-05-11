package com.ecommerce.service;

import com.ecommerce.dto.request.ProductRequest;
import com.ecommerce.dto.response.CategoryResponse;
import com.ecommerce.dto.response.ProductResponse;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(String name, UUID categoryId,
                                         BigDecimal minPrice, BigDecimal maxPrice,
                                         Pageable pageable) {
        return productRepository
                .findWithFilters(name, categoryId, minPrice, maxPrice, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(UUID id) {
        return toResponse(getActiveOrThrow(id));
    }

    public ProductResponse create(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .imageUrl(request.getImageUrl())
                .category(resolveCategory(request.getCategoryId()))
                .build();
        return toResponse(productRepository.save(product));
    }

    public ProductResponse update(UUID id, ProductRequest request) {
        Product product = getOrThrow(id);
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(resolveCategory(request.getCategoryId()));
        return toResponse(productRepository.save(product));
    }

    public void deactivate(UUID id) {
        Product product = getOrThrow(id);
        product.setActive(false);
        productRepository.save(product);
    }

    private Product getActiveOrThrow(UUID id) {
        Product product = getOrThrow(id);
        if (!product.isActive()) {
            throw new ResourceNotFoundException("Produto não encontrado com id: " + id);
        }
        return product;
    }

    private Product getOrThrow(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
    }

    private Category resolveCategory(UUID categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + categoryId));
    }

    private ProductResponse toResponse(Product p) {
        CategoryResponse categoryResponse = p.getCategory() == null ? null :
                CategoryResponse.builder()
                        .id(p.getCategory().getId())
                        .name(p.getCategory().getName())
                        .description(p.getCategory().getDescription())
                        .build();

        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .stock(p.getStock())
                .imageUrl(p.getImageUrl())
                .active(p.isActive())
                .category(categoryResponse)
                .createdAt(p.getCreatedAt())
                .build();
    }
}
