package com.control_api.service;

import com.control_api.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DeleteProductService {

    private final ProductRepository productRepository;

    public DeleteProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void delete(final Long id) {
        final var product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with id: " + id
                ));

        productRepository.delete(product);
    }
}
