package com.control_api.controller;

import com.control_api.dto.ProductRequest;
import com.control_api.dto.ProductResponse;
import com.control_api.service.CreateProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final CreateProductService createProductService;

    public ProductController(final CreateProductService createProductService) {
        this.createProductService = createProductService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody final ProductRequest request,
                                                  final UriComponentsBuilder ucb) {
        var savedProduct = createProductService.create(request.toEntity());
        var response = ProductResponse.fromEntity(savedProduct);
        var location = ucb.path("/products/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }
}
