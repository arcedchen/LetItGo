package com.letitgo.shop.model.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.letitgo.shop.model.entity.Product;

public interface ProductDao extends JpaRepository<Product, Integer> {

	// 搜尋
	List<Product> findTop5ByProductNameContainingAndProductStatus(String productName, String productStatus);

	// 搜尋-可換頁
	Page<Product> findByProductNameContainingAndProductStatus(String productName, String productStatus,
			Pageable pageable);

	// 搜尋-可換頁
	Product findByProductId(Integer id);
	
	// 搜尋-可換頁
	Page<Product> findByProductStatus(String string, Pageable pageable);

	// 搜尋
	@Query(value = "SELECT TOP 5 * FROM Product WHERE product_status = '1' ORDER BY NEWID()", nativeQuery = true)
	List<Product> findTop5Random();

	// 賣家訂單查詢 + 顯示商品
	List<Product> findByMemberId(Integer memberId);

	// 賣家訂單查詢
	Page<Product> findByProductNameContainingAndMemberIdAndProductStatusNot(String searchText, Integer memberId,
			String productStatus, Pageable pageable);
	
	// 搜尋賣家產品
	Page<Product> findByMemberIdAndProductStatus(Integer memberId, String string, Pageable pageable);

	// 搜尋賣家產品
	Page<Product> findByMemberIdAndProductStatusNot(Integer memberId, String productStatus, Pageable pageable);
	
	

}
