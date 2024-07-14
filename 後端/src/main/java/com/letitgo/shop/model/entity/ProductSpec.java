package com.letitgo.shop.model.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "product_spec")
public class ProductSpec {

	public ProductSpec() {
		super();
	}

	public ProductSpec(Integer productSpecId) {
		super();
		this.productSpecId = productSpecId;
	}
	
	@Id
	@Column(name = "product_spec_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productSpecId;

	@Column(name = "product_spec_name", columnDefinition = "nvarchar(100)")
	private String productSpecName;

	@Column(name = "product_origin_price")
	private Integer productOriginPrice;

	@Column(name = "product_selling_price")
	private Integer productSellingPrice;
	
	@Column(name = "stock_quantity")
	private Integer stockQuantity;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private Product product;
	
	@OneToMany(mappedBy = "productSpec" ,cascade = CascadeType.REMOVE)
	private List<ShoppingCartItem> shoppingCartItems;
	
//	@OneToMany(mappedBy = "productSpec")
//	private List<MemberOrderDetail> memberOrderDetail;
}
