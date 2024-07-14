package com.letitgo.shop.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.letitgo.shop.model.entity.Member;
import com.letitgo.shop.model.entity.MemberDetail;

public interface MemberDetailDao extends JpaRepository<MemberDetail, Integer> {

	// 根據會員ID搜尋 MemberPhoto + 更新會員資料 + 搜尋所有賣家
	MemberDetail findByMember(Member m);
}
