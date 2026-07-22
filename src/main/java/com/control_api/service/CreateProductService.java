package com.control_api.service;

import com.control_api.entity.Product;
import com.control_api.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateProductService {
    private final ProductRepository repository;

    public CreateProductService(final ProductRepository repository) {
        this.repository = repository;
    }

    public Product create(final Product product) {
        return repository.save(product);
    }
}
