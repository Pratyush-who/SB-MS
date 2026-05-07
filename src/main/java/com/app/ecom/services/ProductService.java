package com.app.ecom.services;

import com.app.ecom.dto.Product;
import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
        private final ProductRepository productRepository;
    public ProductResponse createProduct(ProductRequest productRequest){
        Product product = new Product();
        updateProductFromRequest(product,productRequest);
        Product savedProd= productRepository.save(product);
        return mapToProductResponse(savedProd);
    }

    private ProductResponse mapToProductResponse(Product savedProd) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(savedProd.getId());
        productResponse.setName(savedProd.getName());
        productResponse.setActive(savedProd.getActive());
        productResponse.setDescription(savedProd.getDescription());
        productResponse.setImageUrl(savedProd.getImageUrl());
        productResponse.setPrice(savedProd.getPrice());
        productResponse.setCategory(savedProd.getCategory());
        productResponse.setStockQuantity(savedProd.getStockQuantity());
        return productResponse;
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setImageUrl(productRequest.getImageUrl());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());
    }
}
