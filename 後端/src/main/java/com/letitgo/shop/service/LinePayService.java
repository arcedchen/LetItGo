package com.letitgo.shop.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import linepay.payment.vo.CheckoutPaymentRequestForm;
import linepay.payment.vo.ConfirmData;
import linepay.payment.vo.PostApiUtil;
import linepay.payment.vo.ProductForm;
import linepay.payment.vo.ProductPackageForm;
import linepay.payment.vo.RedirectUrls;

@Service
public class LinePayService {

	private static String encrypt(final String keys, final String data) {
		return toBase64String(
				HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, keys.getBytes()).doFinal(data.getBytes()));
	}

	private static String toBase64String(byte[] bytes) {
		byte[] byteArray = Base64.encodeBase64(bytes);
		return new String(byteArray);
	}

	// 提供顧客Line Pay付款頁面
	public static String consumerCheck(int amount, String paymentMethodId, String storeName, String productName) {

		CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();
		form.setAmount(amount); // 訂單金額
		form.setCurrency("TWD");
		form.setOrderId(paymentMethodId); // 訂單編號

		ProductPackageForm productPackageForm = new ProductPackageForm();
		productPackageForm.setId(paymentMethodId); // 訂單id
		productPackageForm.setName(storeName); // 商家名稱
		productPackageForm.setAmount(new BigDecimal(amount)); // 訂單金額

		// 商品資訊
		ProductForm productForm = new ProductForm();
		productForm.setId("product_id"); // 不改貌似沒差
		productForm.setName(productName); // Line Pay付款頁面顯示的商品標題
		productForm.setImageUrl(""); // Line Pay付款頁面顯示的商品圖片，用localhost容易有bug
		productForm.setQuantity(new BigDecimal("1")); // 商品數量
		productForm.setPrice(new BigDecimal(amount)); // 商品單價
		productPackageForm.setProducts(Arrays.asList(productForm));

		form.setPackages(Arrays.asList(productPackageForm));
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setConfirmUrl("http://localhost:8080/letitgo/shop/api/linepay/confirm"); // 跳轉到付款成功的頁面
		redirectUrls.setCancelUrl("http://localhost:5173/linepay/error"); // 跳轉到付款失敗的頁面
		form.setRedirectUrls(redirectUrls);
		String channelId = "2004451253";
		String requestUri = "/v3/payments/request";
		String requestHttpsUri = "https://sandbox-api-pay.line.me/v3/payments/request";
		String nonce = UUID.randomUUID().toString();

		ObjectMapper mapper = new ObjectMapper();
		String ChannelSecret = "bc8e81417cf8b8d3438a86661aeaf883"; // Channel Secret Key

		try {
			String signature = encrypt(ChannelSecret,
					ChannelSecret + requestUri + mapper.writeValueAsString(form) + nonce);
			// 組裝responsebody
			JsonNode responsebody = PostApiUtil.sendPost(channelId, nonce, signature, requestHttpsUri,
					mapper.writeValueAsString(form));

			String transactionId = responsebody.path("info").path("transactionId").asText();

			if (transactionId != null) {
				return responsebody.path("info").path("paymentUrl").path("web").asText();
			}

			// 測試用
			System.out.println("signature => " + signature);
			System.out.println("nonce => " + nonce);
			System.out.println("body => " + mapper.writeValueAsString(form));
			System.out.println("Transaction ID: " + transactionId);
			System.out.println("Request API responsebody => " + responsebody);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 顧客付款成功後，確認付款，款項入帳
	public static void confirmApi(int amount, String transactionId, String paymentMethodId) {
		ConfirmData confirmData = new ConfirmData();
		confirmData.setAmount(new BigDecimal(amount)); // 訂單金額
		confirmData.setCurrency("TWD");
		String confirmNonce = UUID.randomUUID().toString();
		String confirmUri = "/v3/payments/" + transactionId + "/confirm";
		String confirmHttpsUri = "https://sandbox-api-pay.line.me/v3/payments/" + transactionId + "/confirm";
		String channelId = "2004451253";
		ObjectMapper mapper = new ObjectMapper();
		String ChannelSecret = "bc8e81417cf8b8d3438a86661aeaf883"; // Channel Secret Key

		try {
			String signatureConfirm = encrypt(ChannelSecret,
					ChannelSecret + confirmUri + mapper.writeValueAsString(confirmData) + confirmNonce);

			// 組裝responsebody
			JsonNode responsebody = PostApiUtil.sendPost(channelId, confirmNonce, signatureConfirm, confirmHttpsUri,
					mapper.writeValueAsString(confirmData));

			// 測試用
			System.out.println("signatureConfirm => " + signatureConfirm);
			System.out.println("Confirm API responsebody => " + responsebody);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
