package com.letitgo.shop.model.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ProductDto {

	private Integer productId;
	private String productName;
	private String productDescription;
	private Integer memberId;
//	private Integer photoId;
	private List<ProductSpecDto> productSpecDtos;
	private Integer productCategoryId;
	private String productCategoryName;
	private String productStatus;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	private Integer totalPage;
}
