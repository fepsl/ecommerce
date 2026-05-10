package com.ecommerce.service;

import com.ecommerce.dto.request.ProductRequest;
import com.ecommerce.dto.response.ProductResponse;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CategoryRepository;
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product activeProduct;

    @BeforeEach
    void setUp() {
        activeProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("Camiseta Teste")
                .price(new BigDecimal("99.90"))
                .stock(10)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("findById retorna produto quando ativo")
    void findById_activeProduct_returnsResponse() {
        when(productRepository.findById(activeProduct.getId()))
                .thenReturn(Optional.of(activeProduct));

        ProductResponse response = productService.findById(activeProduct.getId());

        assertThat(response.getId()).isEqualTo(activeProduct.getId());
        assertThat(response.getName()).isEqualTo("Camiseta Teste");
    }

    @Test
    @DisplayName("findById lança 404 quando produto não existe")
    void findById_notFound_throwsException() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    @DisplayName("findById lança 404 quando produto está inativo")
    void findById_inactiveProduct_throwsException() {
        activeProduct.setActive(false);
        when(productRepository.findById(activeProduct.getId()))
                .thenReturn(Optional.of(activeProduct));

        assertThatThrownBy(() -> productService.findById(activeProduct.getId()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("create salva e retorna produto")
    void create_validRequest_savesAndReturns() {
        ProductRequest request = new ProductRequest();
        request.setName("Novo Produto");
        request.setPrice(new BigDecimal("49.90"));
        request.setStock(5);

        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        ProductResponse response = productService.create(request);

        verify(productRepository, times(1)).save(any(Product.class));
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("deactivate marca produto como inativo")
    void deactivate_existingProduct_setsActiveToFalse() {
        when(productRepository.findById(activeProduct.getId()))
                .thenReturn(Optional.of(activeProduct));
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        productService.deactivate(activeProduct.getId());

        assertThat(activeProduct.isActive()).isFalse();
        verify(productRepository).save(activeProduct);
    }

    @Test
    @DisplayName("findAll sem filtros delega ao repositório com todos os parâmetros nulos")
    void findAll_noFilters_delegatesToRepository() {
        PageRequest pageable = PageRequest.of(0, 12);
        Page<Product> page = new PageImpl<>(List.of(activeProduct));

        when(productRepository.findWithFilters(isNull(), isNull(), isNull(), isNull(), eq(pageable)))
                .thenReturn(page);

        Page<ProductResponse> result = productService.findAll(null, null, null, null, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Camiseta Teste");
        verify(productRepository).findWithFilters(null, null, null, null, pageable);
    }

    @Test
    @DisplayName("findAll com categoria repassa UUID da categoria ao repositório")
    void findAll_withCategoryFilter_passesCategoryIdToRepository() {
        UUID categoryId = UUID.randomUUID();
        PageRequest pageable = PageRequest.of(0, 12);
        Page<Product> page = new PageImpl<>(List.of(activeProduct));

        when(productRepository.findWithFilters(isNull(), eq(categoryId), isNull(), isNull(), eq(pageable)))
                .thenReturn(page);

        Page<ProductResponse> result = productService.findAll(null, categoryId, null, null, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(productRepository).findWithFilters(null, categoryId, null, null, pageable);
    }

    @Test
    @DisplayName("create com categoria inexistente lança ResourceNotFoundException")
    void create_nonExistentCategory_throwsException() {
        UUID categoryId = UUID.randomUUID();
        ProductRequest request = new ProductRequest();
        request.setName("Produto");
        request.setPrice(new BigDecimal("59.90"));
        request.setStock(3);
        request.setCategoryId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(categoryId.toString());

        verify(productRepository, never()).save(any());
    }
}
