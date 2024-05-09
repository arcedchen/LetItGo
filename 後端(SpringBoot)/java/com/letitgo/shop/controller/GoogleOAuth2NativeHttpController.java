package com.letitgo.shop.controller;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letitgo.config.GoogleOauthConfig;
import com.letitgo.shop.model.dao.MemberDao;
import com.letitgo.shop.model.dto.MemberDto;
import com.letitgo.shop.model.entity.Member;
import com.letitgo.shop.model.entity.MemberDetail;
import com.letitgo.shop.service.ShopService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@CrossOrigin(allowCredentials = "true", origins = { "http://localhost:5173/", "http://127.0.0.1:5173" })
public class GoogleOAuth2NativeHttpController {
	// 原生使用(okhttp) http request, response 執行 OAuth2 的寫法

	@Autowired
	private GoogleOauthConfig googleOauth2Config;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private ShopService shopService;

	private final String scope = "https://www.googleapis.com/auth/userinfo.email";

	@GetMapping("/google-login")
	public String googleLogin(HttpServletResponse response) {
		// 直接 redirect 導向 Google OAuth2 API
		String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?" + "client_id="
				+ googleOauth2Config.getClientId() + "&response_type=code" + "&scope=openid%20email%20profile"
				+ "&redirect_uri=" + googleOauth2Config.getRedirectUri() + "&state=state";
		return "redirect:" + authUrl;
	}

	@GetMapping("/google-callback")
	public String oauth2Callback(@RequestParam(required = false) String code, HttpSession session) throws IOException {
		if (code == null) {
			String authUri = "https://accounts.google.com/o/oauth2/v2/auth?response_type=code" + "&client_id="
					+ googleOauth2Config.getClientId() + "&redirect_uri=" + googleOauth2Config.getRedirectUri()
					+ "&scope=" + scope;
			return "redirect:" + authUri;
		} else {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>(); // key 可能重複再用
			map.add("code", code);
			map.add("client_id", googleOauth2Config.getClientId());
			map.add("client_secret", googleOauth2Config.getClientSecret());
			map.add("redirect_uri", googleOauth2Config.getRedirectUri());
			map.add("grant_type", "authorization_code");
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
			ResponseEntity<String> response = restTemplate.postForEntity("https://oauth2.googleapis.com/token", request,
					String.class);
			String credentials = response.getBody();
			// System.out.println("credentials" + credentials);
			JsonNode jsonNode = new ObjectMapper().readTree(credentials);
			String accessToken = jsonNode.get("access_token").asText();
			@SuppressWarnings("unused")
			String idToken = jsonNode.get("id_token").asText();
			HttpHeaders headers2 = new HttpHeaders();
			headers2.setBearerAuth(accessToken);
			HttpEntity<String> entity = new HttpEntity<>(headers2);
			ResponseEntity<String> response2 = restTemplate.exchange(
					"https://www.googleapis.com/oauth2/v1/userinfo?alt=json", HttpMethod.GET, entity, String.class);
			String payloadResponse = response2.getBody();
			JsonNode payloadJsonNode = new ObjectMapper().readTree(payloadResponse);
			// Extract data from payloadJsonNode and process it
			String payloadGoogleId = payloadJsonNode.get("id").asText();
			String payloadEmail = payloadJsonNode.get("email").asText();
			String payloadName = payloadJsonNode.get("name").asText();
			String payloadPicture = payloadJsonNode.get("picture").asText();
			String payloadLocale = payloadJsonNode.get("locale").asText();
			System.out.println("payloadGoogleId: " + payloadGoogleId);
			System.out.println("payloadEmail: " + payloadEmail);
			System.out.println("payloadName: " + payloadName);
			System.out.println("payloadPicture: " + payloadPicture);
			System.out.println("payloadLocale: " + payloadLocale);
//			LoginUserDTO loginUser = new LoginUserDTO(payloadGoogleId, payloadEmail, true);
			MemberDto memberDto = new MemberDto();
			// 找資料庫是否有這個信箱的會員
			Member member = memberDao.findByMemberEmail(payloadEmail);
			if (member == null) {
				// 如果會員不存在，註冊新會員
				Member m = new Member();
				MemberDetail md = new MemberDetail();
				// 儲存google會員資料
				m.setMemberName(payloadName);
				m.setMemberEmail(payloadEmail);
				m.setAccountStatus("1");
				Date d = new Date();
				m.setCreateTime(d);
				m.setLastLoginTime(d);
				md.setMember(m);
				byte[] imageBytes = shopService.downloadImage(payloadPicture);
				// 將byte[]保存到MemberDetail或其他相關的實體中
				md.setMemberPhoto(imageBytes);
				m.setMemberDetail(md);
				// 保存新會員
				member = memberDao.save(m);
			} else {
				// 如果會員已存在，則更新最後登入時間
				if (memberDao.findByMemberEmailAndAccountStatusNot(payloadEmail, "0") != null) {
					member.setLastLoginTime(new Date());
					member = memberDao.save(member);
					BeanUtils.copyProperties(member, memberDto);
					session.setAttribute("loggedInMember", memberDto);
				}else {
					return "redirect:http://localhost:5173/login";
				}
			}
			return "redirect:http://localhost:5173/login/check";
		}
	}

	@GetMapping("/current-user")
	public ResponseEntity<MemberDto> getCurrentUser(HttpSession session) {
		MemberDto loggedInMember = (MemberDto) session.getAttribute("loggedInMember");
		if (loggedInMember != null) {
			return ResponseEntity.ok(loggedInMember);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

}