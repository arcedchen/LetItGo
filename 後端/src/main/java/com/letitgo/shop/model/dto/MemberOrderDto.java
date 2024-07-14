package com.letitgo.shop.model.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MemberOrderDto {
	
	private Integer memberOrderId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date orderDate;
	private String orderStatus;
	private Integer totalAmount;
	private String paymentMethod;
	private String paymentMethodId;
	private Integer memberId;
	private String memberName;
	private String deliveryName;
	private String deliveryPhone;
	private String deliveryAddress;
	private String deliveryMessage;
	private String deliveryMethod;
	private Integer deliveryFee;
	private List<MemberOrderDetailDto> memberOrderDetails;
	private Integer totalPage;
	
	
    
	
}
