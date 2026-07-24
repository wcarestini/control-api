package com.control_api.controller;

import com.control_api.entity.Product;
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
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Should create a product with success and return header location.")
    void shouldCreateProductWithSuccessAndReturnHeaderLocation() throws Exception {
        final var repositoryInitialCount = repository.count();
        final var request = TestProductRequestFactory.aProduct().build();

        final var mvcResult = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", matchesPattern(".*/products/\\d+")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Watch")))
                .andExpect(jsonPath("$.description", is("Smart watch")))
                .andExpect(jsonPath("$.price", is(2500.0)))
                .andExpect(jsonPath("$.quantity", is(3)))
                .andReturn();

        final var responseJson = mvcResult.getResponse().getContentAsString();
        final var createdId = objectMapper.readTree(responseJson).get("id").asLong();

        final var persistedProduct = repository.findById(createdId)
                .orElseThrow(() -> new AssertionError("Product not found with ID: " + createdId));

        assertThat(persistedProduct)
                .returns(request.name(), Product::getName)
                .returns(request.description(), Product::getDescription)
                .returns(request.price(), Product::getPrice)
                .returns(request.quantity(), Product::getQuantity);
        assertEquals(repositoryInitialCount + 1, repository.count());
    }

    @ParameterizedTest
    @ValueSource(strings = {"",
            "   ",
            "  \t  ",
            "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"})
    @NullSource
    @DisplayName("Should throw exception when name is invalid.")
    void shouldThrowExceptionWhenNameIsInvalid(final String invalidName) throws Exception {
        final var request = TestProductRequestFactory.aProduct()
                .withName(invalidName)
                .build();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should throw exception when description is invalid.")
    void shouldThrowExceptionWhenDescriptionIsInvalid() throws Exception {
        final var request = TestProductRequestFactory.aProduct()
                .withDescription("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq")
                .build();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, 0})
    @NullSource
    @DisplayName("Should throw exception when price is invalid.")
    void shouldThrowExceptionWhenPriceIsInvalid(final Double invalidPrice) throws Exception {
        final var request = TestProductRequestFactory.aProduct()
                .withPrice(invalidPrice == null ? null : new BigDecimal(String.valueOf(invalidPrice)))
                .build();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1})
    @NullSource
    @DisplayName("Should throw exception when quantity is invalid.")
    void shouldThrowExceptionWhenQuantityIsInvalid(final Integer invalidQuantity) throws Exception {
        final var request = TestProductRequestFactory.aProduct()
                .withQuantity(invalidQuantity)
                .build();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
