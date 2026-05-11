package com.ecommerce.service;

import com.ecommerce.dto.request.CategoryRequest;
import com.ecommerce.dto.response.CategoryResponse;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService")
@SuppressWarnings("null")
class CategoryServiceTest {

    @Mock private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category(String name) {
        return Category.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description("desc")
                .build();
    }

    private CategoryRequest request(String name) {
        CategoryRequest req = new CategoryRequest();
        req.setName(name);
        req.setDescription("desc");
        return req;
    }

    @Test
    @DisplayName("findAll retorna lista mapeada para response")
    void findAll_returnsMappedList() {
        when(categoryRepository.findAll()).thenReturn(List.of(category("Camisetas"), category("Calças")));

        List<CategoryResponse> result = categoryService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(CategoryResponse::getName)
                .containsExactlyInAnyOrder("Camisetas", "Calças");
    }

    @Test
    @DisplayName("findAll retorna lista vazia sem lançar erro")
    void findAll_emptyRepository_returnsEmptyList() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        assertThat(categoryService.findAll()).isEmpty();
    }

    @Test
    @DisplayName("create salva e retorna a categoria criada")
    void create_savesAndReturnsResponse() {
        Category saved = category("Vestidos");
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        CategoryResponse response = categoryService.create(request("Vestidos"));

        assertThat(response.getName()).isEqualTo("Vestidos");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("update altera nome e descrição da categoria existente")
    void update_existingCategory_updatesFields() {
        Category existing = category("Antigo");
        when(categoryRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        CategoryResponse response = categoryService.update(existing.getId(), request("Novo"));

        assertThat(response.getName()).isEqualTo("Novo");
        verify(categoryRepository).save(existing);
    }

    @Test
    @DisplayName("delete chama deleteById quando categoria existe")
    void delete_existingCategory_callsDeleteById() {
        Category existing = category("Acessórios");
        when(categoryRepository.findById(existing.getId())).thenReturn(Optional.of(existing));

        categoryService.delete(existing.getId());

        verify(categoryRepository).deleteById(existing.getId());
    }

    @Test
    @DisplayName("findById lança ResourceNotFoundException quando id não existe")
    void findById_nonExistingId_throwsException() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    @DisplayName("delete lança ResourceNotFoundException quando id não existe")
    void delete_nonExistingId_throwsException() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.delete(id))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(categoryRepository, never()).deleteById(any());
    }
}
