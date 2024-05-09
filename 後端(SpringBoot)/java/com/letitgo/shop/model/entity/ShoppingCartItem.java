package com.letitgo.shop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "shopping_cart_item")
public class ShoppingCartItem {

	@Id
	@Column(name = "cart_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cartItemId;

	@Column(name = "quantity")
	private Integer quantity;
	
	@ManyToOne
    @JoinColumn(name = "member_id")
	@JsonIgnore
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_spec_id")
    @JsonIgnore
    private ProductSpec productSpec;

}
