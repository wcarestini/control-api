package com.control_api.service;

import com.control_api.entity.Product;
import com.control_api.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FindAllProductsService {

    private final ProductRepository repository;

    public FindAllProductsService(final ProductRepository repository) {
        this.repository = repository;
    }

    public Page<Product> findAll(final Pageable pageable) {
        return repository.findAll(pageable);
    }
}
