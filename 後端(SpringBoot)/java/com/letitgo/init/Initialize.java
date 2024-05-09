package com.letitgo.init;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letitgo.shop.model.dao.MemberDao;
import com.letitgo.shop.model.dao.MemberOrderDao;
import com.letitgo.shop.model.dao.ProductCategoryDao;
import com.letitgo.shop.model.dao.ProductDao;
import com.letitgo.shop.model.entity.Member;
import com.letitgo.shop.model.entity.MemberDetail;
import com.letitgo.shop.model.entity.MemberOrder;
import com.letitgo.shop.model.entity.Product;
import com.letitgo.shop.model.entity.ProductCategory;
import com.letitgo.shop.model.entity.ProductPhoto;

@Component
public class Initialize implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private MemberDao memberDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Autowired
	private MemberOrderDao memberOrderDao;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		addMembers();
//		addProductCategory();
//		addProducts();
//		addOrders();

	}

	private void addMembers() {
		if (memberDao.count() != 0) {
			return;
		}

		try {
			// classpath:init\\members.json
			List<Member> members = new ObjectMapper().readValue(
					ResourceUtils.getFile("src/main/resources/init/members.json"), new TypeReference<List<Member>>() {
					});

			for (int i = 0; i < members.size(); i++) {
				Member member = members.get(i);
				MemberDetail memberDetail = member.getMemberDetail();
				memberDetail.setMember(member);

				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ResourceUtils
						.getFile("src/main/resources/init/image/members/user-type-B-0%s.png".formatted(i + 2))));
				byte[] bytes = bis.readAllBytes();
				bis.close();
				memberDetail.setMemberPhoto(bytes);
			}

			memberDao.saveAll(members);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void addProductCategory() {
		if (productCategoryDao.count() != 0) {
			return;
		}

		try {

			List<ProductCategory> productCategorys = new ObjectMapper().readValue(
					ResourceUtils.getFile("src/main/resources/init/product_category.json"),
					new TypeReference<List<ProductCategory>>() {
					});

			productCategoryDao.saveAll(productCategorys);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void addProducts() {
		if (productDao.count() != 0) {
			return;
		}

		try {

			List<Product> products = new ObjectMapper().readValue(
					ResourceUtils.getFile("src/main/resources/init/products.json"), new TypeReference<List<Product>>() {
					});

			products.forEach(p -> {

				List<ProductPhoto> productPhotos = p.getProductPhotos();
				ProductPhoto productPhoto = productPhotos.get(0);

				try {
					BufferedInputStream bis = new BufferedInputStream(
							new FileInputStream(ResourceUtils.getFile("src/main/resources/init/image/products/%s.jpg"
									.formatted(productPhoto.getProductPhotoId()))));
					byte[] bytes = bis.readAllBytes();

					bis.close();

					productPhoto.setProductPhoto(bytes);
					productPhoto.setProduct(p);
					productPhoto.setSortOrder(1);

				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			productDao.saveAll(products);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void addOrders() {
		if (memberOrderDao.count() != 0) {
			return;
		}

		try {
			List<MemberOrder> memberOrders = new ObjectMapper().readValue(
					ResourceUtils.getFile("src/main/resources/init/member_orders.json"),
					new TypeReference<List<MemberOrder>>() {
					});

			memberOrders.forEach(order -> {
				order.getMemberOrderDetails().forEach(memberOrderDetail -> {
					memberOrderDetail.setMemberOrder(order);
				});
			});

			memberOrderDao.saveAll(memberOrders);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
