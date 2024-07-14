package com.letitgo.shop.model.dto;

import lombok.Data;

@Data
public class ProductSpecDto {
	private Integer productSpecId;
    private String productSpecName;
    private Integer stockQuantity;
    private Integer productOriginPrice;
    private Integer productSellingPrice;
     
    private Integer productId;
    
    private Integer minOriginPrice;
    private Integer maxOriginPrice;
    private Integer minSellingPrice;
    private Integer maxSellingPrice;
	
}
