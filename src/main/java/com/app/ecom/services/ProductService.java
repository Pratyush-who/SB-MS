package com.app.ecom.services;

import com.app.ecom.models.Product;
import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    updateProductFromRequest(existingProduct, productRequest);
                    Product savedProd = productRepository.save(existingProduct);
                    return mapToProductResponse(savedProd);
                });
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public void deleteProduct(Long id) {
        Product product= productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);
        productRepository.delete(product);
    }

    public ResponseEntity<ProductResponse> getProductById(Long id) {
        return productRepository.findById(id)
                .filter(Product::getActive)
                .map(this::mapToProductResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public List<ProductResponse> serchProduct(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }
}
