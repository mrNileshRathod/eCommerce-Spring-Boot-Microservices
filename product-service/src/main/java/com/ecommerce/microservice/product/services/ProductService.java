package com.ecommerce.microservice.product.services;

import com.ecommerce.microservice.product.dto.ProductRequest;
import com.ecommerce.microservice.product.dto.ProductResponse;
import com.ecommerce.microservice.product.model.Product;
import com.ecommerce.microservice.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                    .name(productRequest.name())
                    .description(productRequest.description())
                    .skuCode(productRequest.skuCode())
                    .price(productRequest.price())
                    .build();

        productRepository.save(product);
        System.out.println("Product added successfully");
        return new ProductResponse(product.getId(), product.getSkuCode(), product.getName(), product.getDescription(), product.getPrice());
    }

    public List<ProductResponse> getAllProduct() {
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductResponse(product.getId(), product.getSkuCode(), product.getName(), product.getDescription(), product.getPrice()))
                .toList();
    }
}


