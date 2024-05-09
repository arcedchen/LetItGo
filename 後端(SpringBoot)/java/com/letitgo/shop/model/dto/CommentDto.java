package com.letitgo.shop.model.dto;

import lombok.Data;

@Data
public class CommentDto {
	private Integer memberOrderId;
	private Integer memberId;
	private String memberName;
	private String productSpecName;
	private Integer star;
	private String comment;
	private Integer totalPage;
}