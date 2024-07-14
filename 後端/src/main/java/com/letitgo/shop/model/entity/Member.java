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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "member")
public class Member {

	public Member() {
		super();
	}

	public Member(Integer memberId) {
		super();
		this.memberId = memberId;
	}

	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer memberId;

	@Column(name = "member_name", columnDefinition = "nvarchar(50)")
	private String memberName;

	@Column(name = "member_email", columnDefinition = "nvarchar(50)")
	private String memberEmail;

	@Column(name = "member_password", columnDefinition = "nvarchar(max)")
	private String memberPassword;

	@OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
	private MemberDetail memberDetail;

	@OneToMany(mappedBy = "member")
	private List<MemberOrder> memberOrders;

	@OneToMany(mappedBy = "member")
	private List<ShoppingCartItem> shoppingCartItems;

	@Column(name = "member_phone")
	private String memberPhone;

	@Column(name = "account_status")
	private String accountStatus;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "create_time")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "last_login_time")
	private Date lastLoginTime;

}
