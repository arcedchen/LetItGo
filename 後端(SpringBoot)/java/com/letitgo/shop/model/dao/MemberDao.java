package com.letitgo.shop.model.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.letitgo.shop.model.entity.Member;

public interface MemberDao extends JpaRepository<Member, Integer> {

	// 搜尋-賣家
	List<Member> findTop5ByMemberNameContainingAndAccountStatus(String searchText, String accountStatus);

	// 搜尋-賣家-可換頁
	Page<Member> findByMemberNameContainingAndAccountStatus(String searchText, String accountStatus, Pageable pageable);

	// 搜尋-賣家-可換頁
	Page<Member> findByAccountStatus(String accountStatus, Pageable pageable);

	// 搜尋會員資料 + 更新會員資料
	Member findByMemberId(Integer memberId);

	// 登入
	Member findByMemberEmailAndMemberPasswordAndAccountStatusNot(String email, String password, String accountStatus);

	// google登入
	Member findByMemberEmailAndAccountStatusNot(String email, String accountStatus);

	// 註冊
	Member findByMemberEmail(String email);

}
