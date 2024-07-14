package com.letitgo.shop.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ReportDto {

	private Integer reportId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	private Integer memberId;
	private Integer productId;
	private Integer orderId;
	private String reportMessage;
	private String otherMessage;
	private String reportStatus;
	private Integer totalPage;

}
