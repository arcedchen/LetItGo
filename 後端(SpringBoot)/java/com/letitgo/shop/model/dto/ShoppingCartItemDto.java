package com.letitgo.shop.model.dto;

import lombok.Data;

@Data
public class ShoppingCartItemDto {

	private Integer memberId; // 賣家
	private Integer productId;
	private String productName;
	private Integer productPhotoId;
	private String productStatus;
	private Integer productSpecId;
	private String productSpecName;
	private Integer productSellingPrice;
	private Integer quantity;
	private Integer stockQuantity;
}
