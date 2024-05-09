package com.letitgo.shop.model.entity;

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
@Table(name = "member_order_detail")
public class MemberOrderDetail {

	@Id
	@Column(name = "order_detail_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderDetailId;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "unit_price")
	private Integer unitPrice;

	@Column(name = "product_name", columnDefinition = "nvarchar(100)")
	private String productName;

	@ManyToOne
	@JoinColumn(name = "member_order_id")
	private MemberOrder memberOrder;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "star")
	private Integer star;

	@Column(name = "comment", columnDefinition = "nvarchar(MAX)")
	private String comment;
	
//	@ManyToOne
//	@JoinColumn(name = "product_spec_id")
//	private ProductSpec productSpec; 
	
	@Column(name = "product_spec_id")
	private Integer productSpecId;
	
	@Column(name = "product_spec_name", columnDefinition = "nvarchar(50)")
	private String productSpecName;
	
	
}
