package com.letitgo.shop.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class TradeDto {

	private Integer tradeId;

	private Integer memberId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	private Integer change;

	private Integer walletAmount;

	private String status;
	
	private Integer totalPage;
}
