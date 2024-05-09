package com.letitgo.shop.model.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "trade")
public class Trade {

	@Id
	@Column(name = "trade_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tradeId;

	@Column(name = "member_id")
	private Integer memberId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "update_time")
	private Date updateTime;
	
	@Column(name = "change")
	private Integer change;

	@Column(name = "wallet_amount")
	private Integer walletAmount;
	
	@Column(name = "status", columnDefinition = "nvarchar(50)")
	private String status;

}
