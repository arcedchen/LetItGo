package com.letitgo.shop.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MemberDto {

	private Integer memberId;
	private String memberName;
	
	private String memberEmail;
	private String memberPassword;
	private String memberPhone;
	private String accountStatus;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lastLoginTime;
	
	private Integer memberAge;
	private String memberGender;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthDate;
	private String memberAddress;
	private String memberPhoto;
	
	private Integer totalPage;
}
