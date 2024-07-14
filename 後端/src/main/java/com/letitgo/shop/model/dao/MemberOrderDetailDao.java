package com.letitgo.shop.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.letitgo.shop.model.entity.MemberOrderDetail;
import com.letitgo.shop.model.entity.Product;

public interface MemberOrderDetailDao extends JpaRepository<MemberOrderDetail, Integer> {

	//賣家訂單查詢
	List<MemberOrderDetail> findByProductIsIn(List<Product> products);

	// 更新評價
	MemberOrderDetail findByOrderDetailId(Integer orderDetailId);

	// 商品頁面展示評論(會員id、會員名稱、購買規格、評價星級、評論內容)
	List<MemberOrderDetail> findByProductSpecIdAndStarNotNull(Integer productSpecId);

}
