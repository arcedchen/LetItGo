package com.letitgo.shop.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.letitgo.shop.model.entity.Product;
import com.letitgo.shop.model.entity.ProductSpec;

public interface ProductSpecDao extends JpaRepository<ProductSpec, Integer> {

	// 新增刪除購物車商品
	ProductSpec findByProductSpecId(Integer productSpecId);

	// 顯示賣家商品 
	List<ProductSpec> findByProduct(Product productId);

	// 根據商品ID搜尋 ProductSpec 最高最低價
	ProductSpec findTop1ByProductOrderByProductSellingPriceAsc(Product p);

	ProductSpec findTop1ByProductOrderByProductSellingPriceDesc(Product p);

	ProductSpec findTop1ByProductOrderByProductOriginPriceAsc(Product p);

	ProductSpec findTop1ByProductOrderByProductOriginPriceDesc(Product p);

	// 搜尋賣家產品
	List<ProductSpec> findByProductProductId(Integer productId);

}