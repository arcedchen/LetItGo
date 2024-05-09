package com.letitgo.shop.model.dto;

import lombok.Data;

@Data
public class MemberOrderDetailDto {

	private Integer orderDetailId;
	private Integer quantity;
	private Integer unitPrice;
	private Integer memberOrderId;
	private Integer star;
	private String comment;
	private Integer productId;
	private Integer productSpecId;
	private String productName;
	private String productSpecName;

}
