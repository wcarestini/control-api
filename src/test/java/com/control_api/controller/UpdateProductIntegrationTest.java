package com.control_api.controller;

import com.control_api.entity.Product;
import com.control_api.factory.entity.TestProductFactory;
import com.control_api.factory.request.TestProductRequestFactory;
import com.control_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdateProductIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    ProductRepository repository;

    private final String PATH = "/products/";

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Should update product")
    void shouldUpdateProduct() throws Exception {
        final var product = repository.save(TestProductFactory.iphone().build());
        final var productRequest = TestProductRequestFactory.aProduct().build();

        mockMvc.perform(put(PATH + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(product.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Watch")))
                .andExpect(jsonPath("$.description", is("Smart watch")))
                .andExpect(jsonPath("$.price", is(2500.0)))
                .andExpect(jsonPath("$.quantity", is(3)));

        final var persistedProduct = repository.findById(product.getId())
                .orElseThrow(() -> new AssertionError("Product not found with ID: " + product.getId()));

        assertThat(persistedProduct)
                .returns(productRequest.name(), Product::getName)
                .returns(productRequest.description(), Product::getDescription)
                .returns(productRequest.price(), Product::getPrice)
                .returns(productRequest.quantity(), Product::getQuantity);
    }

    @Test
    @DisplayName("Should throw exception when product not found")
    void shouldThrowExceptionWhenProductNotFound() throws Exception {
        final var nonExistentId = 1;
        final var productRequest = TestProductRequestFactory.aProduct().build();

        mockMvc.perform(put(PATH + nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail", is("Product not found with ID: " + nonExistentId)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"",
            "   ",
            "  \t  ",
            "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"})
    @NullSource
    @DisplayName("Should throw exception when name is invalid.")
    void shouldThrowExceptionWhenNameIsInvalid(final String invalidName) throws Exception {
        final var id = 1;
        final var request = TestProductRequestFactory.aProduct()
                .withName(invalidName)
                .build();

        mockMvc.perform(put(PATH + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should throw exception when description is invalid.")
    void shouldThrowExceptionWhenDescriptionIsInvalid() throws Exception {
        final var id = 1;
        final var request = TestProductRequestFactory.aProduct()
                .withDescription("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq")
                .build();

        mockMvc.perform(put(PATH + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, 0})
    @NullSource
    @DisplayName("Should throw exception when price is invalid.")
    void shouldThrowExceptionWhenPriceIsInvalid(final Double invalidPrice) throws Exception {
        final var id = 1;
        final var request = TestProductRequestFactory.aProduct()
                .withPrice(invalidPrice == null ? null : new BigDecimal(String.valueOf(invalidPrice)))
                .build();

        mockMvc.perform(put(PATH + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1})
    @NullSource
    @DisplayName("Should throw exception when quantity is invalid.")
    void shouldThrowExceptionWhenQuantityIsInvalid(final Integer invalidQuantity) throws Exception {
        final var id = 1;
        final var request = TestProductRequestFactory.aProduct()
                .withQuantity(invalidQuantity)
                .build();

        mockMvc.perform(put(PATH + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
