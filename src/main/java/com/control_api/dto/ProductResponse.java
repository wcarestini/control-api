package com.control_api.dto;

import com.control_api.entity.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer quantity
) {
    public static ProductResponse fromEntity(final Product entity) {
        return new ProductResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getQuantity()
        );
    }
}
