package com.letitgo.shop.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.letitgo.shop.model.entity.Product;
import com.letitgo.shop.model.entity.ProductPhoto;

public interface ProductPhotoDao extends JpaRepository<ProductPhoto, Integer> {
	
	// 根據產品ID搜尋商品圖片
	ProductPhoto findByProductAndSortOrder(Product product, Integer sortOrder);
	
	// 修改賣家商品
	ProductPhoto findByProduct(Product p);

}
