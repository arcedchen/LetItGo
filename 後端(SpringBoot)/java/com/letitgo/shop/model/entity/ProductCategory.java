package com.letitgo.shop.model.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "product_category")
public class ProductCategory {

	@Id
	@Column(name = "product_category_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productCategoryId;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "parent_id")
	@JsonIgnoreProperties(value = "parentCategory")
	private ProductCategory parentCategory;

	@Column(name = "sort_order")
	private Integer sortOrder;

	@Column(name = "category_name", columnDefinition = "nvarchar(50)")
	private String categoryName;

	@Column(name = "category_description", columnDefinition = "nvarchar(100)")
	private String categoryDescription;

	@Column(name = "category_status", columnDefinition = "nvarchar(50)")
	private String categoryStatus;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Column(name = "create_time")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Column(name = "update_time")
	private Date updateTime;

//	@OneToMany(mappedBy = "productCategory")
//	@JsonIgnore
//	private List<Product> products;
	
	

}
