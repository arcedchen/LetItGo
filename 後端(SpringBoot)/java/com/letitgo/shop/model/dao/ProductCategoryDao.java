package com.letitgo.shop.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.letitgo.shop.model.entity.ProductCategory;

public interface ProductCategoryDao extends JpaRepository<ProductCategory, Integer> {
	
	// 新增產品分類
	ProductCategory findByProductCategoryId(Integer parentId);
	
	// 刪除商品分類
	List<ProductCategory> findByParentCategory(ProductCategory pc);

}
