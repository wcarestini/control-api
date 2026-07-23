package com.control_api.controller;

import com.control_api.dto.ProductRequest;
import com.control_api.dto.ProductResponse;
import com.control_api.service.CreateProductService;
import com.control_api.service.FindAllProductsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final CreateProductService createProductService;
    private final FindAllProductsService findAllProductsService;

    public ProductController(final CreateProductService createProductService,
                             final FindAllProductsService findAllProductsService) {
        this.createProductService = createProductService;
        this.findAllProductsService = findAllProductsService;
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

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findAll(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.ASC
            )
            final Pageable pageable
    ) {
        var productList = findAllProductsService.findAll(pageable);
        var response = productList.map(ProductResponse::fromEntity);

        return ResponseEntity.ok(response);
    }
}
