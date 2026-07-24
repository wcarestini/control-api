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

import java.math.BigDecimal;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FindAllProductsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        repository.saveAll(TestProductFactory.defaultProductList());
    }


    @Test
    @DisplayName("Should return 10 products when page is 0 and size is 10")
    void shouldReturnTenProductsWhenPageIsZeroAndSizeIsTen() throws Exception {
        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(10))
                .andExpect(jsonPath("$.totalElements").value(15));
    }

    @Test
    @DisplayName("Should return 5 products when page is 1 and size is 10")
    void shouldReturnFiveProductsWhenPageIsOneAndSizeIsTen() throws Exception {
        mockMvc.perform(get("/products")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(5))
                .andExpect(jsonPath("$.totalElements").value(15));
    }

    @Test
    @DisplayName("Should return price sorted list.")
    void shouldReturnPriceSortedList() throws Exception {
        var expectedPrices = TestProductFactory.defaultProductList().stream()
                .map(Product::getPrice)
                .sorted()
                .map(BigDecimal::doubleValue)
                .toArray();

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "15")
                        .param("sort", "price,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].price").value(contains(expectedPrices)));
    }

    @Test
    @DisplayName("Should return empty list when page no exist.")
    void shouldReturnEmptyListWhenPageNoExist() throws Exception {
        mockMvc.perform(get("/products")
                        .param("page", "999")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.empty").value(true))
                .andExpect(jsonPath("$.totalElements").value(15));
    }

    @Test
    @DisplayName("Should return empty list when has no products.")
    void shouldReturnEmptyListWhenHasNoProducts() throws Exception {
        repository.deleteAll();

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.empty").value(true))
                .andExpect(jsonPath("$.totalElements").value(0));
    }
}
