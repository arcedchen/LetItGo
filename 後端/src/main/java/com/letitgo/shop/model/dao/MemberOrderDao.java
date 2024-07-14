package com.letitgo.shop.model.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.letitgo.shop.model.entity.MemberOrder;

public interface MemberOrderDao extends JpaRepository<MemberOrder, Integer> {

	// 賣家買家訂單查詢
	Page<MemberOrder> findBySellerIdOrderByOrderDateDesc(Integer memberId, Pageable pageable);

	// 賣家買家訂單查詢
	Page<MemberOrder> findBySellerIdAndOrderStatusOrderByOrderDateDesc(Integer memberId, String status,
			Pageable pageable);

	// 賣家買家訂單查詢
	Page<MemberOrder> findByMemberMemberIdOrderByOrderDateDesc(Integer memberId, Pageable pageable);

	// 賣家買家訂單查詢
	Page<MemberOrder> findByMemberMemberIdAndOrderStatusOrderByOrderDateDesc(Integer memberId, String string,
			Pageable pageable);

	// 賣家買家訂單查詢
	Page<MemberOrder> findBySellerIdAndMemberOrderIdOrderByOrderDateDesc(
			Integer memberId, Integer searchText, Pageable pageable);

	// 賣家買家訂單查詢
	Page<MemberOrder> findBySellerIdAndMemberOrderIdAndOrderStatusOrderByOrderDateDesc(
			Integer memberId, Integer searchText, String status, Pageable pageable);

	// 賣家買家訂單查詢
	Page<MemberOrder> findBySellerIdAndMemberMemberNameContainingOrderByOrderDateDesc(
			Integer memberId, String searchText, Pageable pageable);

	// 賣家買家訂單查詢
	Page<MemberOrder> findBySellerIdAndMemberMemberNameContainingAndOrderStatusOrderByOrderDateDesc(
			Integer memberId, String searchText, String status, Pageable pageable);

	// 賣家訂單狀態修改
	MemberOrder findByMemberOrderId(Integer memberOrderId);

	// pay + linepay
	MemberOrder findByPaymentMethodId(String paymentMethodId);

}
