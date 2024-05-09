package com.letitgo.shop.model.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product {

	public Product() {
		super();
	}

	public Product(Integer productId) {
		super();
		this.productId = productId;
	}

	@Id
	@Column(name = "product_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productId;

	@Column(name = "product_name", columnDefinition = "nvarchar(100)")
	private String productName;

	@Column(name = "product_description", columnDefinition = "nvarchar(max)")
	private String productDescription;

	@Column(name = "product_status")
	private String productStatus;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "create_time")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "update_time")
	private Date updateTime;

//	@ManyToOne
//	@JoinColumn(name = "product_category_id")
//	private ProductCategory productCategory;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<ProductPhoto> productPhotos;

	@OneToMany(mappedBy = "product")
	@JsonIgnore
	private List<MemberOrderDetail> memberOrderDetails;

	@OneToMany(mappedBy = "product")
	private List<ShoppingCartItem> shoppingCartItems;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<ProductSpec> productSpecs;

	@Column(name = "member_id")
	private Integer memberId;
	
	@Column(name = "product_category_id")
	private Integer productCategoryId;

}
