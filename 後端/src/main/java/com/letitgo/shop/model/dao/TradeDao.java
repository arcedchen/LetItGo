package com.letitgo.shop.model.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.letitgo.shop.model.entity.Trade;

public interface TradeDao extends JpaRepository<Trade, Integer> {

	
	List<Trade> findByMemberIdOrderByUpdateTimeDesc(Integer memberId);

	
	boolean existsByMemberId(Integer memberId);

	
	Trade findTradeByTradeId(Integer tradeId);

	// 會員錢包
	Page<Trade> findByMemberIdOrderByUpdateTimeDesc(Integer memberId, Pageable pageable);

}
