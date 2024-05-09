package com.letitgo.shop.model.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "member_order")
public class MemberOrder {

	@Id
	@Column(name = "member_order_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer memberOrderId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "order_date")
	private Date orderDate;

	@Column(name = "order_status", columnDefinition = "nvarchar(50)")
	private String orderStatus;

	@Column(name = "total_amount")
	private Integer totalAmount;

	@Column(name = "payment_method", columnDefinition = "nvarchar(50)")
	private String paymentMethod;

	@Column(name = "payment_method_id")
	private String paymentMethodId;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "memberOrder", cascade = CascadeType.ALL)
	private List<MemberOrderDetail> memberOrderDetails;

	@Column(name = "delivery_name", columnDefinition = "nvarchar(50)")
	private String deliveryName;

	@Column(name = "delivery_phone")
	private String deliveryPhone;

	@Column(name = "delivery_address", columnDefinition = "nvarchar(100)")
	private String deliveryAddress;

	@Column(name = "delivery_message", columnDefinition = "nvarchar(250)")
	private String deliveryMessage;

	@Column(name = "delivery_method", columnDefinition = "nvarchar(50)")
	private String deliveryMethod;
	
	@Column(name = "delivery_fee")
	private Integer deliveryFee;
	
	@Column(name = "seller_id")
	private Integer sellerId;

}
