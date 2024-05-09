package com.letitgo.shop.model.entity;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "member_detail")
public class MemberDetail {
	
	@Id
	@GeneratedValue(generator="generator")
	@GenericGenerator(name="generator",strategy = "foreign",parameters=@Parameter(name="property",value="member"))
	@Column(name = "member_id")
	private Integer memberId;

	@OneToOne
	@PrimaryKeyJoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_member_detail_member", 
			foreignKeyDefinition = "FOREIGN KEY (member_id) REFERENCES Member(member_id) ON DELETE CASCADE ON UPDATE CASCADE"))
//	@MapsId("memberId")
	private Member member;

	@Column(name = "member_age")
	private Integer memberAge;

	@Column(name = "member_gender", columnDefinition = "nvarchar(50)")
	private String memberGender;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "birth_date")
	private Date birthDate;

	@Column(name = "member_address", columnDefinition = "nvarchar(100)")
	private String memberAddress;

	@Column(name = "member_photo", columnDefinition = "varbinary(max)")
	private byte[] memberPhoto;

	
}
