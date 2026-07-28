package com.control_api.service;

import com.control_api.dto.ProductRequest;
import com.control_api.entity.Product;
import com.control_api.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UpdateProductService {

    private final ProductRepository repository;

    public UpdateProductService(final ProductRepository repository) {
        this.repository = repository;
    }

    public Product update(final Long id, final ProductRequest productRequest) {
        final var product = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Product not found with ID: " + id));

        product.update(
                productRequest.name(),
                productRequest.description(),
                productRequest.price(),
                productRequest.quantity());

        return repository.save(product);
    }
}
