package com.control_api.service;

import com.control_api.entity.Product;
import com.control_api.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FindProductByIdService {

    private final ProductRepository repository;

    public FindProductByIdService(final ProductRepository repository) {
        this.repository = repository;
    }

    public Product findById(final Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with ID: " + id
                ));
    }
}
