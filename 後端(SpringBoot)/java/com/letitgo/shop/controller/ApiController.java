package com.letitgo.shop.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letitgo.shop.model.dto.CommentDto;
import com.letitgo.shop.model.dto.MemberDto;
import com.letitgo.shop.model.dto.MemberOrderDetailDto;
import com.letitgo.shop.model.dto.MemberOrderDto;
import com.letitgo.shop.model.dto.ProductCategoryDto;
import com.letitgo.shop.model.dto.ProductDto;
import com.letitgo.shop.model.dto.ProductSpecDto;
import com.letitgo.shop.model.dto.ReportDto;
import com.letitgo.shop.model.dto.SearchDto;
import com.letitgo.shop.model.dto.ShoppingCartItemDto;
import com.letitgo.shop.model.dto.TradeDto;
import com.letitgo.shop.model.entity.Product;
import com.letitgo.shop.model.entity.ProductCategory;
import com.letitgo.shop.model.entity.ProductSpec;
import com.letitgo.shop.model.entity.Report;
import com.letitgo.shop.model.entity.ShoppingCartItem;
import com.letitgo.shop.model.entity.Trade;
import com.letitgo.shop.service.ShopService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/shop/api")
@CrossOrigin(allowCredentials = "true", origins = { "http://localhost:5173/", "http://127.0.0.1:5173" })
public class ApiController {

	@Autowired
	private ShopService shopService;

	// 搜尋
	@GetMapping("/searchProduct/{searchMode}/{searchText}")
	public List<SearchDto> findProductBySearch(@PathVariable String searchMode, @PathVariable String searchText) {
		return shopService.findProductBySearch(searchMode, searchText);
	}

	// 收尋-可換頁
	@GetMapping("/searchProduct/{searchMode}/{searchText}/{pageNumber}")
	public List<SearchDto> findProductBySearchAndPage(@PathVariable String searchMode, @PathVariable String searchText,
			@PathVariable Integer pageNumber, HttpSession session) {
		return shopService.findProductBySearchAndPage(searchMode, searchText, pageNumber);
	}

	// 收尋會員-可換頁
	@GetMapping("/member/product/{memberId}/{pageNumber}")
	public List<SearchDto> findProductByMemberIdAndPage(@PathVariable Integer memberId,
			@PathVariable Integer pageNumber, HttpSession session) {
		return shopService.findProductByMemberIdAndPage(memberId, pageNumber);
	}

	// 根據產品ID搜尋 Product
	@GetMapping("/product/{id}")
	public ProductDto findProductByProductId(@PathVariable Integer id) {
		return shopService.findProductByProductId(id);
	}

	// 根據產品ID搜尋 ProductPhoto
	@GetMapping(path = "/product/photo/{id}", produces = "image/*")
	public ResponseEntity<byte[]> findProductPhotoByProductId(@PathVariable Integer id) {
		return shopService.findProductPhotoByProductId(id);
	}

	// 根據產品ID搜尋 ProductSpec
	@GetMapping(path = "/product/spec/{id}")
	public List<ProductSpec> findProductSpecByProductId(@PathVariable Integer id) {
		return shopService.findProductSpecByProductId(id);
	}

	// 根據產品ID搜尋 ProductSpec 最高最低價
	@GetMapping(path = "/product/spec/price/{id}")
	public ProductSpecDto findProductSpecPriceByProductId(@PathVariable Integer id) {
		return shopService.findProductSpecPriceByProductId(id);
	}

	// 根據規格ID更新規格資料
	@PostMapping(path = "/product/spec/{quantity}/{id}")
	public boolean updateProductSpecByProductSpecId(@PathVariable Integer id, @PathVariable Integer quantity) {
		return shopService.updateProductSpecByProductSpecId(id, quantity);
	}

	// 根據會員ID搜尋 MemberPhoto
	@GetMapping(path = "/member/photo/{id}", produces = "image/*")
	public ResponseEntity<byte[]> findMemberPhotoByMemberId(@PathVariable Integer id) {
		return shopService.findMemberPhotoByMemberId(id);
	}

	// 搜尋所有會員
	@GetMapping("/member/all/{pageNumber}")
	public List<MemberDto> findAllMember(@PathVariable Integer pageNumber) {
		return shopService.findAllMember(pageNumber);
	}

	// 根據會員id修改會員狀態
	@PostMapping("/member/updateMemberStatus/{memberId}")
	public boolean updateMemberStatus(@PathVariable("memberId") Integer memberId) {
		return shopService.updateMemberStatus(memberId);
	}

	// 搜尋會員資料
	@GetMapping("/member/{id}")
	public MemberDto findMemberByMemberId(@PathVariable Integer id) {
		return shopService.findMemberByMemberId(id);
	}

	// 更新會員資料
	@PostMapping("/member/update")
	public boolean updateMemberByMemberId(@RequestParam("member") String memberDataDtoJson,
			@RequestParam(name = "memberPhoto", required = false) MultipartFile memberPhoto, HttpSession session)
			throws IOException {
		MemberDto loggedInMember = (MemberDto) session.getAttribute("loggedInMember");
		return shopService.updateMemberByMemberId(memberDataDtoJson, memberPhoto, loggedInMember);
	}

	// 搜尋賣家產品
	@GetMapping("/member/searchProduct/{searchText}/{pageNumber}")
	public List<ProductDto> findProductBySearchText(@PathVariable String searchText, @PathVariable Integer pageNumber,
			HttpSession session) {
		MemberDto loggedInMember = (MemberDto) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			throw new RuntimeException("登入失敗");
		}
		return shopService.findByProductNameContainingAndMemberIdAndProductStatusNot(searchText,
				loggedInMember.getMemberId(), pageNumber);
	}

	// 登入
	@RequestMapping("/login")
	public MemberDto login(@RequestParam String email, @RequestParam String password, HttpSession session) {
		MemberDto loggedInMember = shopService.login(email, password);
		if (loggedInMember == null) {
			throw new RuntimeException("登入失敗");
		}
		session.setAttribute("loggedInMember", loggedInMember);
		return loggedInMember;
	}

	@RequestMapping("/check")
	public boolean checkLogin(HttpSession session) {
		MemberDto loggedInMember = (MemberDto) session.getAttribute("loggedInMember");
		return !Objects.isNull(loggedInMember);
	}

	// 登出
	@RequestMapping("/logout")
	public boolean logout(HttpSession session) {
		session.invalidate();
		return true;
	}

	// 註冊會員
	@RequestMapping("/register")
	public boolean register(@RequestParam String name, @RequestParam String email, @RequestParam String password,
			HttpSession session) {
		boolean register = shopService.register(name, email, password);
		if (register == false) {
			throw new RuntimeException("註冊失敗");
		}
		return register;
	}

	// 新增商品分類
	@PostMapping("/addProductCategory")
	public boolean addProductCategory(@RequestParam("productCategory") String productCategory,
			@RequestParam(name = "parentId", required = false) String parentId) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ProductCategory pc = objectMapper.readValue(productCategory, ProductCategory.class);
		return shopService.addProductCategory(pc, parentId);
	}

	// 顯示商品分類
	@GetMapping("/productCategory")
	public List<ProductCategory> productCategory() {
		return shopService.findAll();
	}

	// 顯示商品分類
	@GetMapping("/productCategory/{pageNumber}")
	public List<ProductCategoryDto> productCategory(@PathVariable Integer pageNumber) {
		return shopService.findProductCategory(pageNumber);
	}

	// 修改商品類別
	@PostMapping("/updateProductCategory")
	public ProductCategory updateProductCategory(@RequestParam("editedCategory") String editedCategoryJson)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		ProductCategoryDto pcDto = objectMapper.readValue(editedCategoryJson, ProductCategoryDto.class);
		return shopService.updateProductCategory(pcDto);

	}

	// 刪除商品分類
	@PostMapping("/deleteProductCategory")
	public boolean deleteProductCategory(@RequestParam("productCategoryId") String productCategoryId)
			throws JsonMappingException, JsonProcessingException {
		return shopService.deleteProductCategory(productCategoryId);

	}

	// 新增商品
	@PostMapping("/addProduct")
	public boolean addProduct(@RequestParam("productDto") String productDtoJson,
			@RequestParam("productSpecDto") String productSpecDtoJson,
			@RequestParam("productPhoto") MultipartFile productPhoto, HttpSession session) throws IOException {
		// 將 JSON 字串轉換為 ProductDto 物件
		ObjectMapper objectMapper = new ObjectMapper();
		ProductDto productDto = objectMapper.readValue(productDtoJson, ProductDto.class);
		List<ProductSpecDto> productSpecs = objectMapper.readValue(productSpecDtoJson,
				new TypeReference<List<ProductSpecDto>>() {
				});

		byte[] productPictureTop = productPhoto.getBytes();
		MemberDto loggedInMember = (MemberDto) session.getAttribute("loggedInMember");
		boolean addProduct = shopService.addProduct(productDto, productSpecs, productPictureTop,
				loggedInMember.getMemberId());
		if (addProduct == false) {
			throw new RuntimeException("新增商品失敗");
		}
		return addProduct;
	}

	// 修改賣家商品
	@PostMapping("/product/updateProduct")
	public boolean updateProductByProductId(@RequestParam("productData") String productDataJson,
			@RequestParam("productSpecs") String productSpecsJson,
			@RequestParam("deleteProductSpecId") String deleteProductSpecIdJson,
			@RequestParam(value = "productPhoto", required = false) MultipartFile productPhoto) throws IOException {
		return shopService.updateProductByProductId(productDataJson, deleteProductSpecIdJson, productSpecsJson,
				productPhoto);
	}

	// 賣家變更商品上下架狀態
	@PostMapping("/product/updateProductStatus/{productId}")
	public boolean updateProductStatusByProductId(@PathVariable Integer productId) {
		return shopService.updateProductStatusByProductId(productId);
	}

	// 賣家商品下架 狀態改9
	@PostMapping("/product/deleteProductStatus/{productId}")
	public boolean deleteProductStatusByProductId(@PathVariable Integer productId) {
		return shopService.deleteProductStatusByProductId(productId);
	}

	// 新增刪除購物車商品
	@RequestMapping("/product/{mode}/{productSpecId}/{quantity}")
	public ShoppingCartItem productToCart(@PathVariable String mode, @PathVariable Integer productSpecId,
			@PathVariable Integer quantity, HttpSession session) {
		MemberDto loggedInMember = (MemberDto) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			throw new RuntimeException("未登入錯誤");
		}
		ShoppingCartItem ProductToCart = new ShoppingCartItem();
		if (mode.equals("plus")) {
			ProductToCart = shopService.plusProductToCart(loggedInMember.getMemberId(), productSpecId);
		}
		if (mode.equals("minus")) {
			ProductToCart = shopService.minusProductToCart(loggedInMember.getMemberId(), productSpecId);
		}
		if (mode.equals("create")) {
			ProductToCart = shopService.createProductToCart(loggedInMember.getMemberId(), productSpecId, quantity);
		}
		if (mode.equals("delete")) {
			ProductToCart = shopService.deleteProductToCart(loggedInMember.getMemberId(), productSpecId);
		}
		if (ProductToCart == null) {
			throw new RuntimeException("沒有這個mode");
		}
		return ProductToCart;
	}

	// 顯示購物車
	@RequestMapping("/cart")
	public List<ShoppingCartItemDto> findCartByMemberId(HttpSession session) {
		MemberDto loggedInMember = (MemberDto) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			throw new RuntimeException("未登入錯誤");
		}
		List<ShoppingCartItemDto> cartItems = shopService.findShoppingCartByMemberId(loggedInMember.getMemberId());
		return cartItems;
	}

	// 新增訂單
	@PostMapping("/addOrder")
	public String addOrder(@RequestParam("memberOrderDto") String memberOrderDtoJson,
			@RequestParam("memberOrderDetailDto") String memberOrderDetailDtoJson, HttpSession session)
			throws IOException {
		MemberDto loggedInMember = (MemberDto) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			throw new RuntimeException("未登入錯誤");
		}
		ObjectMapper objectMapper = new ObjectMapper();
		MemberOrderDto memberOrderDto = objectMapper.readValue(memberOrderDtoJson, MemberOrderDto.class);
		List<MemberOrderDetailDto> memberOrderDetailDto = objectMapper.readValue(memberOrderDetailDtoJson,
				new TypeReference<List<MemberOrderDetailDto>>() {
				});
		String UUID = shopService.addOrder(memberOrderDto, memberOrderDetailDto, loggedInMember.getMemberId());
		return UUID;
	}

	// 賣家買家訂單查詢
	@GetMapping("/order/{mode}/{searchText}/{pageNumber}")
	public List<MemberOrderDto> findMemberOrder(@PathVariable Integer mode, @PathVariable String searchText,
			@PathVariable Integer pageNumber, HttpSession session) {
		MemberDto loggedInMember = (MemberDto) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			throw new RuntimeException("未登入錯誤");
		}
		return shopService.findMemberOrder(loggedInMember.getMemberId(), mode, searchText, pageNumber);
	}

	// 修改訂單狀態
	@PostMapping("/order/updateOrder/{orderStatus}/{memberOrderId}")
	public boolean updateMemberOrderByMemberId(@PathVariable String orderStatus, @PathVariable Integer memberOrderId,
			HttpSession session) {
		MemberDto loggedInMember = (MemberDto) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			throw new RuntimeException("未登入錯誤");
		}
		return shopService.updateMemberOrderByMemberIdToCancelled(orderStatus, memberOrderId);
	}

	// 更新評價
	@PostMapping("/order/updateOrderComment")
	public boolean updateOrderComment(@RequestParam("comments") String comments)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<MemberOrderDetailDto> modDtos = objectMapper.readValue(comments,
				new TypeReference<List<MemberOrderDetailDto>>() {
				});
		return shopService.updateOrderComment(modDtos);
	}

	// 商品頁面展示評論(會員id、會員名稱、購買規格、評價星級、評論內容)
	@GetMapping("/product/memberCommemt/{id}")
	public List<CommentDto> memberCommemt(@PathVariable("id") Integer productId) {
		return shopService.memberCommemt(productId);
	}

	// 賣家訂單狀態修改
	@PostMapping("/order/updateOrder/orderStatus/{memberOrderId}/{trackingNumber}")
	public boolean updateMemberOrderByMemberId(@PathVariable Integer memberOrderId, @PathVariable String trackingNumber,
			HttpSession session) throws IOException {
		MemberDto loggedInMember = (MemberDto) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			throw new RuntimeException("未登入錯誤");
		}
		return shopService.updateMemberOrderByMemberId(memberOrderId, trackingNumber);
	}

	// pay
	@PostMapping("/pay/{UUID}")
	public String pay(@PathVariable String UUID, HttpSession session) throws IOException {
		MemberDto loggedInMember = (MemberDto) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			throw new RuntimeException("未登入錯誤");
		}
		return shopService.pay(UUID);
	}

	// linepay
	@GetMapping("/linepay/confirm")
	public ModelAndView confirmApi(@RequestParam String transactionId, @RequestParam String orderId) {
		// https://pay.line.me/portal/tw/auth/login
		// 帳號：test_202404012550@line.pay
		// 密碼：PySZf!D2B0
		// 顧客付款成功後，確認付款，款項入帳
		// 在這個功能中，orderId實際上是paymentMethodId
		return shopService.confirmApi(transactionId, orderId);
	}

	// 會員錢包
	@GetMapping("/trade/{memberId}/{pageNumber}")
	public ResponseEntity<List<TradeDto>> findTradeByMemberId(@PathVariable Integer memberId,
			@PathVariable Integer pageNumber) {
		List<TradeDto> trades = shopService.findTradeByMemberId(memberId, pageNumber);
		return ResponseEntity.ok(trades);
	}

	@PostMapping("/report")
	public void submitReport(@RequestBody Report report) {
		shopService.submitReport(report);
	}

	// 顯示所有 report
	@GetMapping("/report/all/{pageNumber}")
	public List<ReportDto> findAllReports(@PathVariable Integer pageNumber) {
		return shopService.findAllReportsSortedByCreateTimeDesc(pageNumber);
	}

	// 根據 report 內容修改 report 狀態(成立)
	@PostMapping("report/changeReportTo8")
	public boolean changeReportYes(@RequestParam("report") String reportJson, HttpSession session)
			throws JsonMappingException, JsonProcessingException {
		MemberDto loggedInMember = (MemberDto) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			throw new RuntimeException("未登入錯誤");
		}
		ObjectMapper objectMapper = new ObjectMapper();
		ReportDto rd = objectMapper.readValue(reportJson, ReportDto.class);

		return shopService.changeReportYes(rd);
	}

	// 根據 report 內容修改 report 狀態(不成立)
	@PostMapping("report/changeReportNot0")
	public boolean changeReportNot(@RequestParam("report") String reportJson, HttpSession session)
			throws JsonMappingException, JsonProcessingException {
		MemberDto loggedInMember = (MemberDto) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			throw new RuntimeException("未登入錯誤");
		}
		ObjectMapper objectMapper = new ObjectMapper();
		ReportDto rd = objectMapper.readValue(reportJson, ReportDto.class);

		return shopService.changeReportNot(rd);
	}

	// 所有交易
	@GetMapping("/trade/all/{pageNumber}")
	public ResponseEntity<List<TradeDto>> getAllTrades(@PathVariable Integer pageNumber) {
		List<TradeDto> trades = shopService.getAllTradesSortedByUpdateTimeDesc(pageNumber);
		return ResponseEntity.ok(trades);
	}

// =============================================================================

	// 為會員創建新錢包
	@PostMapping("/trade/createWallet")
	public ResponseEntity<Trade> insertTrade(@RequestParam Integer memberId) {
		Trade newTrade = shopService.createWallet(memberId);
		return ResponseEntity.ok(newTrade);
	}

	// 依據 memberId 選擇並只顯示最新一筆資料
	@GetMapping("/trade/latest/{memberId}")
	public ResponseEntity<Trade> getLatestTradeByMemberId(@PathVariable Integer memberId) {
		Trade latestTrade = shopService.getLatestTradeByMemberId(memberId);
		if (latestTrade != null) {
			return ResponseEntity.ok(latestTrade);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// 根據 memberId 和 change 新增交易
	@PostMapping("/trade/createTrade")
	public ResponseEntity<Trade> createTrade(@RequestParam Integer memberId, @RequestParam Integer change) {
		Trade newTrade = shopService.createTrade(memberId, change);
		if (newTrade != null) {
			return ResponseEntity.ok(newTrade);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// 管理員進行撥款
	@PutMapping("/trade/agree")
	public void agreeTrade(@RequestParam Integer tradeId) {
		shopService.agreeTrade(tradeId);
	}

	// 訂單狀態設為已完成，並將款項存入賣家錢包
	@PutMapping("/orders/finish")
	public void finishMemberOrder(@RequestParam Integer memberOrderId) {
		shopService.finishMemberOrder(memberOrderId);
	}

}
