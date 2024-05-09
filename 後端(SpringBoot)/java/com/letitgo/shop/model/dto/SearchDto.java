package com.letitgo.shop.model.dto;

import lombok.Data;

@Data
public class SearchDto {
	private Integer productId;
	private String productName;
	
	private Integer memberId;
	private String memberName;
	
	private Integer totalPage;
	private Integer minPrice;
	private Integer maxPrice;
}
