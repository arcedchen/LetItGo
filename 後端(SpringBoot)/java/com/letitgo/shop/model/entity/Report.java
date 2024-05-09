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

@Data
@Entity
@Table(name = "report")
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
	private Integer reportId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "member_id")
	private Integer memberId;

	@Column(name = "product_id")
	private Integer productId;

	@Column(name = "order_id")
	private Integer orderId;

	@Column(name = "report_message", columnDefinition = "nvarchar(100)")
	private String reportMessage;

	@Column(name = "other_message", columnDefinition = "nvarchar(100)")
	private String otherMessage;

	@Column(name = "report_status", columnDefinition = "nvarchar(50)")
	private String reportStatus;

}