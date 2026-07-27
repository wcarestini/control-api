package com.control_api.controller;

import com.control_api.factory.entity.TestProductFactory;
import com.control_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FindProductByIdIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Should return product when id exists.")
    void shouldReturnProductWhenIdExists() throws Exception {
        final var product = repository.save(TestProductFactory.notebook().build());

        mockMvc.perform(get("/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Notebook")))
                .andExpect(jsonPath("$.description", is("Notebook dell")))
                .andExpect(jsonPath("$.price", is(1200.00)))
                .andExpect(jsonPath("$.quantity", is(23)));
    }

    @Test
    @DisplayName("Should return 404 when product does not exist.")
    void shouldReturn404WhenProductDoesNotExist() throws Exception {
        mockMvc.perform(get("/products/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Product not found with ID: 2"));
    }
}
