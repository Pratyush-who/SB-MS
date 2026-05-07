package com.app.ecom.dto;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String price;
    private String stockQuantity;
    private String category;
    private String imageUrl;
    private Boolean active;
}
