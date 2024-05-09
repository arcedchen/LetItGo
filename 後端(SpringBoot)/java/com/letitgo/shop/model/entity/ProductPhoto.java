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
@Table(name = "product_photo")
public class ProductPhoto {

	@Id
	@Column(name = "product_photo_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productPhotoId;

	@Column(name = "product_photo", columnDefinition = "varbinary(max)")
	private byte[] productPhoto;

	@Column(name = "sort_order")
	private Integer sortOrder;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private Product product;

}
