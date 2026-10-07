package com.control_api.controller;

import com.control_api.entity.Product;
import com.control_api.factory.entity.TestProductFactory;
import com.control_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repository;

    private final String PATH = "/products/";

    @BeforeEach
    void setup() {
        final var products = TestProductFactory.defaultProductList();
        repository.saveAll(products);
    }

    @Test
    @DisplayName("Should return 204 when deleting a product")
    void shouldReturn204WhenDeletingAProduct() throws Exception {
        final var id = repository.findAll().getFirst().getId();

        mockMvc.perform(delete(PATH + id))
                .andExpect(status().isNoContent());

        assertThat(repository.existsById(id))
                .isFalse();
    }

    @Test
    @DisplayName("Should return 404 when product not found with id")
    void shouldReturn404WhenProductNotFoundWithId() throws Exception {
        mockMvc.perform(delete(PATH + 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail", is("Product not found with id: " + 999)));
    }

    @Test
    @DisplayName("Should return 400 when id is invalid")
    void shouldReturn400WhenIdIsInvalid() throws Exception {
        mockMvc.perform(delete(PATH + "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", is("Failed to convert 'id' with value: " + "'abc'")));
    }
}
