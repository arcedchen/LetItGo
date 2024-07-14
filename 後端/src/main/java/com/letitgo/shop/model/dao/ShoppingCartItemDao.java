package com.letitgo.shop.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.letitgo.shop.model.entity.Member;
import com.letitgo.shop.model.entity.ProductSpec;
import com.letitgo.shop.model.entity.ShoppingCartItem;

public interface ShoppingCartItemDao extends JpaRepository<ShoppingCartItem, Integer> {

	// 新增商品到購物車
	ShoppingCartItem findByMemberAndProductSpec(Member memberId, ProductSpec productSpecId);

	// 顯示購物車
	List<ShoppingCartItem> findByMember(Member memberId);
}
