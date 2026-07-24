package com.control_api.factory.request;

import com.control_api.dto.ProductRequest;

import java.math.BigDecimal;

public final class TestProductRequestFactory {

    public static ProductRequestBuilder aProduct() {
        return new ProductRequestBuilder();
    }

    public static class ProductRequestBuilder {
        private String name = "Watch";
        private String description = "Smart watch";
        private BigDecimal price = new BigDecimal("2500.00");
        private Integer quantity = 3;

        private ProductRequestBuilder() {}

        public ProductRequestBuilder withName(final String name) {
            this.name = name;
            return this;
        }

        public ProductRequestBuilder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public ProductRequestBuilder withPrice(final BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductRequestBuilder withQuantity(final Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public ProductRequest build() {
            return new ProductRequest(name, description, price, quantity);
        }
    }
}
