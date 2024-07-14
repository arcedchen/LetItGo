package com.letitgo.shop.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProductCategoryDto {

    private Integer productCategoryId;
    private Integer parentCategoryId;
	private String parentCategoryName;
    private Integer sortOrder;
    private String categoryName;
    private String categoryDescription;
    private String categoryStatus;
    private Date createTime;
    private Date updateTime;
    private Integer totalPage;

}
