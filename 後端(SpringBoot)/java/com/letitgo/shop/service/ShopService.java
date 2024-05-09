package com.letitgo.shop.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letitgo.shop.model.dao.MemberDao;
import com.letitgo.shop.model.dao.MemberDetailDao;
import com.letitgo.shop.model.dao.MemberOrderDao;
import com.letitgo.shop.model.dao.MemberOrderDetailDao;
import com.letitgo.shop.model.dao.ProductCategoryDao;
import com.letitgo.shop.model.dao.ProductDao;
import com.letitgo.shop.model.dao.ProductPhotoDao;
import com.letitgo.shop.model.dao.ProductSpecDao;
import com.letitgo.shop.model.dao.ReportDao;
import com.letitgo.shop.model.dao.ShoppingCartItemDao;
import com.letitgo.shop.model.dao.TradeDao;
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
import com.letitgo.shop.model.entity.Member;
import com.letitgo.shop.model.entity.MemberDetail;
import com.letitgo.shop.model.entity.MemberOrder;
import com.letitgo.shop.model.entity.MemberOrderDetail;
import com.letitgo.shop.model.entity.Product;
import com.letitgo.shop.model.entity.ProductCategory;
import com.letitgo.shop.model.entity.ProductPhoto;
import com.letitgo.shop.model.entity.ProductSpec;
import com.letitgo.shop.model.entity.Report;
import com.letitgo.shop.model.entity.ShoppingCartItem;
import com.letitgo.shop.model.entity.Trade;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@Service
public class ShopService {
	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Autowired
	private ProductSpecDao productSpecDao;

	@Autowired
	private ProductPhotoDao productPhotoDao;

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private MemberDetailDao memberDetailDao;

	@Autowired
	private MemberOrderDao memberOrderDao;

	@Autowired
	private MemberOrderDetailDao memberOrderDetailDao;

	@Autowired
	private ShoppingCartItemDao shoppingCartItemDao;

	@Autowired
	private TradeDao tradeDao;

	@Autowired
	private ReportDao reportDao;

	// 搜尋
	public List<SearchDto> findProductBySearch(String searchMode, String searchText) {
		// 1:搜尋商品 2:搜尋賣家 3:隨機搜尋
		if (searchMode.equals("1")) {
			List<Product> p = productDao.findTop5ByProductNameContainingAndProductStatus(searchText, "1");
			List<SearchDto> searchDtos = p.stream().map(product -> {
				SearchDto searchDto = new SearchDto();
				BeanUtils.copyProperties(product, searchDto);
				return searchDto;
			}).collect(Collectors.toList());
			return searchDtos;
		} else if (searchMode.equals("2")) {
			List<Member> m = memberDao.findTop5ByMemberNameContainingAndAccountStatus(searchText, "1");
			List<SearchDto> searchDtos = m.stream().map(member -> {
				SearchDto searchDto = new SearchDto();
				BeanUtils.copyProperties(member, searchDto);
				return searchDto;
			}).collect(Collectors.toList());
			return searchDtos;
		} else if (searchMode.equals("3")) {
			List<Product> p = productDao.findTop5Random();
			List<SearchDto> searchDtos = p.stream().map(product -> {
				SearchDto searchDto = new SearchDto();
				BeanUtils.copyProperties(product, searchDto);
				Product p1 = productDao.findByProductId(product.getProductId());
				ProductSpec ps1 = productSpecDao.findTop1ByProductOrderByProductSellingPriceAsc(p1);
				ProductSpec ps2 = productSpecDao.findTop1ByProductOrderByProductSellingPriceDesc(p1);
				searchDto.setMinPrice(ps1.getProductSellingPrice());
				searchDto.setMaxPrice(ps2.getProductSellingPrice());
				return searchDto;
			}).collect(Collectors.toList());
			return searchDtos;
		}
		return null;
	}

	// 搜尋-可換頁
	public List<SearchDto> findProductBySearchAndPage(String searchMode, String searchText, Integer pageNumber) {
		// 1:搜尋商品 2:搜尋賣家
		// 11:賣家頁面狀態搜尋
		if (searchMode.equals("1")) {
			if (searchText.equals("all")) {
				Pageable pageable = PageRequest.of(pageNumber, 10);
				Page<Product> productPage = productDao.findByProductStatus("1", pageable);
				List<Product> productList = productPage.getContent();
				List<SearchDto> searchDtos = productList.stream().map(product -> {
					SearchDto searchDto = new SearchDto();
					BeanUtils.copyProperties(product, searchDto);
					int totalPage = productPage.getTotalPages();
					searchDto.setTotalPage(totalPage);
					Product p = productDao.findByProductId(product.getProductId());
					ProductSpec ps1 = productSpecDao.findTop1ByProductOrderByProductSellingPriceAsc(p);
					ProductSpec ps2 = productSpecDao.findTop1ByProductOrderByProductSellingPriceDesc(p);
					searchDto.setMinPrice(ps1.getProductSellingPrice());
					searchDto.setMaxPrice(ps2.getProductSellingPrice());
					return searchDto;
				}).collect(Collectors.toList());
				return searchDtos;
			} else {
				Pageable pageable = PageRequest.of(pageNumber, 10);
				Page<Product> productPage = productDao.findByProductNameContainingAndProductStatus(searchText, "1",
						pageable);
				List<Product> productList = productPage.getContent();
				List<SearchDto> searchDtos = productList.stream().map(product -> {
					SearchDto searchDto = new SearchDto();
					BeanUtils.copyProperties(product, searchDto);
					int totalPage = productPage.getTotalPages();
					searchDto.setTotalPage(totalPage);
					Product p = productDao.findByProductId(product.getProductId());
					ProductSpec ps1 = productSpecDao.findTop1ByProductOrderByProductSellingPriceAsc(p);
					ProductSpec ps2 = productSpecDao.findTop1ByProductOrderByProductSellingPriceDesc(p);
					searchDto.setMinPrice(ps1.getProductSellingPrice());
					searchDto.setMaxPrice(ps2.getProductSellingPrice());
					return searchDto;
				}).collect(Collectors.toList());
				return searchDtos;
			}
		} else if (searchMode.equals("2")) {
			if (searchText.equals("all")) {
				Pageable pageable = PageRequest.of(pageNumber, 10);
				Page<Member> memberPage = memberDao.findByAccountStatus("1", pageable);
				List<Member> memberList = memberPage.getContent();
				List<SearchDto> searchDtos = memberList.stream().map(member -> {
					SearchDto searchDto = new SearchDto();
					BeanUtils.copyProperties(member, searchDto);
					int totalPage = memberPage.getTotalPages();
					searchDto.setTotalPage(totalPage);
					return searchDto;
				}).collect(Collectors.toList());
				return searchDtos;
			} else {
				Pageable pageable = PageRequest.of(pageNumber, 10);
				Page<Member> memberPage = memberDao.findByMemberNameContainingAndAccountStatus(searchText, "1",
						pageable);
				List<Member> memberList = memberPage.getContent();
				List<SearchDto> searchDtos = memberList.stream().map(member -> {
					SearchDto searchDto = new SearchDto();
					BeanUtils.copyProperties(member, searchDto);
					int totalPage = memberPage.getTotalPages();
					searchDto.setTotalPage(totalPage);
					return searchDto;
				}).collect(Collectors.toList());
				return searchDtos;
			}
		}
		return null;
	}

	// 收尋會員-可換頁
	public List<SearchDto> findProductByMemberIdAndPage(Integer memberId, Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 10);
		Page<Product> productPage = productDao.findByMemberIdAndProductStatus(memberId, "1", pageable);
		List<Product> productList = productPage.getContent();
		List<SearchDto> searchDtos = productList.stream().map(product -> {
			SearchDto searchDto = new SearchDto();
			BeanUtils.copyProperties(product, searchDto);
			int totalPage = productPage.getTotalPages();
			searchDto.setTotalPage(totalPage);
			Product p = productDao.findByProductId(product.getProductId());
			ProductSpec ps1 = productSpecDao.findTop1ByProductOrderByProductSellingPriceAsc(p);
			ProductSpec ps2 = productSpecDao.findTop1ByProductOrderByProductSellingPriceDesc(p);
			searchDto.setMinPrice(ps1.getProductSellingPrice());
			searchDto.setMaxPrice(ps2.getProductSellingPrice());
			return searchDto;
		}).collect(Collectors.toList());
		return searchDtos;
	}

	// 根據產品ID搜尋 Product
	public ProductDto findProductByProductId(Integer id) {
		Product p = productDao.findByProductId(id);
		ProductDto pd = new ProductDto();
		BeanUtils.copyProperties(p, pd);
		return pd;
	}

	// 根據產品ID搜尋 ProductPhoto
	public ResponseEntity<byte[]> findProductPhotoByProductId(Integer id) {
		byte[] productPhoto = productPhotoDao.findByProductAndSortOrder(productDao.findById(id).get(), 1)
				.getProductPhoto();
		if (productPhoto != null) {
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(productPhoto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// 根據產品ID搜尋 ProductSpec
	public List<ProductSpec> findProductSpecByProductId(Integer id) {
		Product p = productDao.findByProductId(id);
		List<ProductSpec> ps = productSpecDao.findByProduct(p);
		return ps;
	}

	// 根據產品ID搜尋 ProductSpec 最高最低價
	public ProductSpecDto findProductSpecPriceByProductId(Integer id) {
		Product p = productDao.findByProductId(id);
		// 低價
		ProductSpec ps1 = productSpecDao.findTop1ByProductOrderByProductSellingPriceAsc(p);
		ProductSpec ps2 = productSpecDao.findTop1ByProductOrderByProductOriginPriceAsc(p);
		// 高價
		ProductSpec ps3 = productSpecDao.findTop1ByProductOrderByProductSellingPriceDesc(p);
		ProductSpec ps4 = productSpecDao.findTop1ByProductOrderByProductOriginPriceDesc(p);
		ProductSpecDto psd = new ProductSpecDto();
		psd.setMaxOriginPrice(ps4.getProductOriginPrice());
		psd.setMinOriginPrice(ps2.getProductOriginPrice());
		psd.setMaxSellingPrice(ps3.getProductSellingPrice());
		psd.setMinSellingPrice(ps1.getProductSellingPrice());
		return psd;
	}

	// 根據規格ID更新規格資料
	public boolean updateProductSpecByProductSpecId(Integer id, Integer quantity) {
		ProductSpec ps = productSpecDao.findByProductSpecId(id);
		ps.setStockQuantity(quantity);
		productSpecDao.save(ps);
		return true;
	}

	// 根據會員ID搜尋 MemberPhoto
	public ResponseEntity<byte[]> findMemberPhotoByMemberId(Integer id) {
		byte[] memberPhoto = memberDetailDao.findByMember(memberDao.findById(id).get()).getMemberPhoto();

		if (memberPhoto != null) {
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(memberPhoto);
		} else {
			String s = "524946469E080000574542505650384C920800002F2BC14A0077A5A06D1BA9FC4967FF768760FEE77FFE15B96DDB649D9296EFE00ACAC9980224E6868F13482321149B60344D7B0DA64D2A3603465155B83445AE09563218D01883FDFFFF1FA76D3EF9E67AA895ACAA0AA15451A8F75EF5DE1BEFBD91B15935C17BEFA96028A584602A534A4BA52A52555DEE3EFF646C4977FA7EDE5F7E7344FF27803FFBFFB3FF3FFBFFB3FF67183DE73FEF07DF3C7B78FFFEFDA7AF06C6A61D6F46C29978D9B57F65224CD57E11F9E386F34F26DC9983E260F7B6A44DFE8696B78FB8E637FD705FD2A2804676BE720C2EFF647723053CDC3A6264DEC8C5D98AEAB2E5AE63586EBA3546751C6D2F9A93DBBF3B4CF5FEEB54C98CC68E44498BB36E78C653FCAE85F4991C349B1F76DAA4D7ED3F198BFB743EE937F2D44CCA57BF223D6F299A47F15284B41D1F328C522A4C3AB73A3D83707A22A4FBF52553F01E379280C92933189D4F3246860DA0D0AA484AFB257C0F2324A8BA85DDF42A12B60BB97B21123705DB4FEB48E21468033192B90B31AF4D91D437F12AAE22B9D573B4324D24B93D84553A44B2872791BAA548FA4411A74B04E05A0F24EF2441780523EF1061680D437494506C2A03749E703C804F2F01A986D079A990A0848BCD8F0D84650F34F93881192A00E32D23388F037391F0B4B3B00C2940E8182AE5AF09D18602288709D32E4C4614284D1E22DE6C427500913B04EB0E40CABFC525E4E07185807D03472982CC01387A08D9460F0CB7111A9A06E32561FB108C95E01CC022ABC09983450F816BBB50CC47872690C82B785E22F188E0ED4462273EAD48340523B6AE75D7424B53E1D5FB5A37C47D5909448182D8FCDA6366CE9F521A8A3EFC9999D91B68F1E11B200682B0A8C49F7C6D69A739C79F74D6D42E0C446F00A205AEB05737F60457586EAA19B9381C0C401F57EA3669E60457FCBC76391C56FA67152BE294663295FD1CAED9071C92FECDE6CA07F412F62AE395351BC621E2DF9A2A26F4F20D57B9A766DFE3608996A86677CD9EC3E090FFC92AD27A0979552CA9D97D188A0150F9CA4EEB85462A2B37D4EC260C8500507B45E5A866765776936AFE1D0CA52084A62A394D9A55C395E4A3B5EB82C1090235673FD547DA8D8E7F2A3F876A7F0906B68240E1DE22337BA3EB48C3F6C5DC2F4AB7A2E4631B0ED1401059C9E58BA2A469D5BC6C458B45BE9EC7211910E1CFE0B06A46E5E08CCAD519957E842EE29047A80D078E03D409C446807A81E803E8161019801E01E1C5F1790D041FC0E72F48F4E3338E843B0B9E1C127C58AED0A9E17CEEFBEDCA37178AD120D9CDCDB6467616F8A323BFF329CC58CE0ECC927E97D979354F134DEFF8D353517F9260DC0988EAE58F7B6D3A5047CB5CE91B7F5683510E07E3065798AABF3F0C73954B7D3908069F0EC47AAED45B5267EA8CC3D53EF1A5078D692B00EA4345FC5ED5D5B73F70F5395F5EA1C15B03D0C2556EAD23FB92CBB5B4FC9884633400BBAA998CD5CDF249AEA9A77CB05D3878B97F07AAE1C9447DC49F728D27C9C7D98CE7807F4BABE2F26115BC8654996BDDE9C76E4078BE6F56BE2AE6CC06152CFB709E6B5E8CFA711391B46FD45E03E6E9F6A40A4CE878966BEFAC263FC710E1B9BE45CBB560E6A9EBEB2201508BEFFC8F7DCC2E253F432E2469DFA8BD46CCEC4D3C3CBE2CF665ED625BEEE7D84FEF7A887C5DC5982EF4AD61BA661F2F4DF43FEC3E7B60EB9AA50B667FDBFCCBE4DC153BDA9E4F79EC6F6601F9DC0DCAA06FB4D4F3A74E9D368BFCFE3B28BCCA37BAA89F9124F9FE9587CA98F24D3DD64CF9A822FF0F33AC3B7D23EB9556DE3652100771C936F846D67D7DE4365320632E2E9CF28FE884AB07B73744C13CC1C0961B0340F327743090A4807E318E0CBF0802D9A972BD65D65060E733B62B834014BFEBD6D3BF775B14DC47E04CDA81208AF714EAE51F7B2C0A70CC0187DB0242646F4BBBC1F306D62A0A7427A3EB24824244E16D8FF3819ABED24C010F97E0E161151C2252897D77C69C20B8C36DB329F8ED0CF091407D5435AF3B75E3ED78C1AB8D571C79707CB14DF5182D23546E0ADCA7AD6862E1AA2D7B0E9FBE90BADC7D3975FEC4BE8D4B1321AADF3B0CF190AA17DDCEF630E2D310A85106D96941E014C39C69902FE1E0C4B7C4B3C718E9CDD2DD60A84B4DB2ED64B0C76CC9163868F11DC17EFF13E3BD57ACD81403EECC176A568621CFC5458A6518F4B106811A2719F6B425CEEC1C03FF4009B3A1CCD0F789A2DA3D06FFCF4A8ED800E37F4349B1A1C026F8CC1621FA940D7138AA3FD55A6063CC2ED4DDD21FD924DD94A5B3D96936CDF185DA5AF8C663F3F45E2475646DF91B1BAA975E676926D1F95F36D9D28B238B943EC27936DE2BA4D135C6F35CE984EE1BCE5488B41ACE198DB78434BBC5681E9176DF198C13D74FC23597EBA4E13E63F1BED651A4642AAF48CB6DA6F2273D850A6692B7F4446D66D2479A8E948C64B1AEA8CF44F296B69A3C03B94FFA7E6B201B34B6C63CDCB0C6ACAC710C91CE3B8DE3ACD692C6D1A235CA184656E9EDA261DC26BD270D638DE6E85F46516ED0DD35A37843BA5F6314ADDA0BB906E1C5B44723063142FAEF34887302AC318884001173982409A78DA14B8497C6B040840BA6905722AC3585DB24629329AC9241396650FA950C943183A724E42B33D82245AF1138BF91E2A811F49394EB8DE09018734DC06B14236E02EF494CDB3380763954D100E6CB4153F8E59520A3F83D2441DFE1B7559217F0B9B3247900DF28497A1DBECBA274C1B75494147A655B94F3E80D90A867D1BB20CB71F416C972043CC79E4919A69994EE19950D332A71614E60972761CF629796E63C7657A469C36EAB341DD825A5B90A9D67497313BA7F92B40FA04B8BF30CBA3E71DE4077429C41E83689330ADD02713E40D7284E16BA066954013947496339C8E549DA06467E4A9C2874E3E23443372ACE1CE8FE2ACED219950DD08D8AB313BA8C3847A1CB8A7311BAFF2B697AA0E3903477B14B4AF31CBB0DD2F46397926604BBB43419EC4A4A982C76BC50165504EFAA302E78394B149BD1DF2C4A04BE312549237CBC5592A408";
			int len = s.length();
			byte[] b = new byte[len / 2];
			for (int i = 0; i < len; i += 2) {
				b[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
			}
//			return ResponseEntity.notFound().build();
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(b);
		}
	}

	// 搜尋所有賣家
	public List<MemberDto> findAllMember(Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 5);
		Page<Member> memberPage = memberDao.findAll(pageable);
		List<MemberDto> mDtos = new ArrayList<>();
		for (Member member : memberPage) {
			MemberDetail md = memberDetailDao.findByMember(member);
			MemberDto mDto = new MemberDto();
			// 會員類別資料
			mDto.setMemberId(member.getMemberId());
			mDto.setMemberName(member.getMemberName());
			mDto.setMemberEmail(member.getMemberEmail());
			mDto.setMemberPhone(member.getMemberPhone());
			mDto.setAccountStatus(member.getAccountStatus());
			mDto.setCreateTime(member.getCreateTime());
			mDto.setLastLoginTime(member.getLastLoginTime());
			// 會員詳細資料
			mDto.setMemberAge(md.getMemberAge());
			mDto.setMemberGender(md.getMemberGender());
			mDto.setBirthDate(md.getBirthDate());
			mDto.setMemberAddress(md.getMemberAddress());
			int totalPage = memberPage.getTotalPages();
			mDto.setTotalPage(totalPage);
			mDtos.add(mDto);
		}
		return mDtos;
	}

	// 修改會員狀態(停權復權共用)
	public boolean updateMemberStatus(Integer memberId) {
		Member member = memberDao.findByMemberId(memberId);
		String status = member.getAccountStatus();
		if ("1".equals(status)) {
			member.setAccountStatus("0");
			memberDao.save(member);
			return true;
		} else if ("0".equals(status)) {
			member.setAccountStatus("1");
			memberDao.save(member);
			return true;
		} else {
			return false;
		}
	}

	// 搜尋會員資料
	public MemberDto findMemberByMemberId(Integer id) {
		Member m = memberDao.findByMemberId(id);
		MemberDto memberDto = new MemberDto();
		BeanUtils.copyProperties(m, memberDto);
		MemberDetail md = memberDetailDao.findByMember(m);
		memberDto.setMemberAge(md.getMemberAge());
		memberDto.setMemberGender(md.getMemberGender());
		memberDto.setBirthDate(md.getBirthDate());
		memberDto.setMemberAddress(md.getMemberAddress());
		if (md.getMemberPhoto() != null) {
			memberDto.setMemberPhoto("NotNull");
		}
		return memberDto;
	}

	// 更新會員資料
	public boolean updateMemberByMemberId(String memberDataDtoJson, MultipartFile memberPhoto, MemberDto loggedInMember)
			throws IOException {
		Member m = memberDao.findByMemberId(loggedInMember.getMemberId());
		MemberDetail md = memberDetailDao.findByMember(m);
		// 將 JSON 字串轉換為 MemberDto 物件
		ObjectMapper objectMapper = new ObjectMapper();
		MemberDto memberDto = objectMapper.readValue(memberDataDtoJson, MemberDto.class);
		// 將memberDto裡有的同名變數而m(member物件也有的)值傳給m
		BeanUtils.copyProperties(memberDto, m);
		// 将memberDto的属性复制到md对象中，但不包括setMemberPhoto
		BeanUtils.copyProperties(memberDto, md, "memberPhoto");
		if (memberPhoto != null && m.getMemberDetail() != null) {
			m.getMemberDetail().setMemberPhoto(memberPhoto.getBytes());
		}
		memberDao.save(m);
		return true;
	}

	// 搜尋賣家產品
	public List<ProductDto> findByProductNameContainingAndMemberIdAndProductStatusNot(String searchText,
			Integer memberId, Integer pageNumber) {
		if (!searchText.equals("all")) {
			Pageable pageable = PageRequest.of(pageNumber, 5);
			Page<Product> productPage = productDao.findByProductNameContainingAndMemberIdAndProductStatusNot(searchText,
					memberId, "9", pageable);
			List<ProductDto> productDtos = productPage.getContent().stream().map(product -> {
				ProductDto pDto = new ProductDto();
				BeanUtils.copyProperties(product, pDto);
				ProductCategory pc = productCategoryDao.findByProductCategoryId(product.getProductCategoryId());
				if (pc != null) {
					pDto.setProductCategoryName(pc.getCategoryName());
				}
				if (pc == null) {
					pDto.setProductCategoryName("尚無類別，請新增");
				}
				int totalPage = productPage.getTotalPages();
				pDto.setTotalPage(totalPage);
				List<ProductSpec> ps = productSpecDao.findByProductProductId(pDto.getProductId());
				List<ProductSpecDto> psDtos = ps.stream().map(spec -> {
					ProductSpecDto specDto = new ProductSpecDto();
					BeanUtils.copyProperties(spec, specDto);
					return specDto;
				}).collect(Collectors.toList());
				pDto.setProductSpecDtos(psDtos);
				return pDto;
			}).collect(Collectors.toList());
			return productDtos;
		} else {
			Pageable pageable = PageRequest.of(pageNumber, 5);
			Page<Product> productPage = productDao.findByMemberIdAndProductStatusNot(memberId, "9", pageable);
			List<ProductDto> productDtos = productPage.getContent().stream().map(product -> {
				ProductDto pDto = new ProductDto();
				BeanUtils.copyProperties(product, pDto);
				ProductCategory pc = productCategoryDao.findByProductCategoryId(product.getProductCategoryId());
				if (pc != null) {
					pDto.setProductCategoryName(pc.getCategoryName());
				}
				if (pc == null) {
					pDto.setProductCategoryName("尚無類別，請新增");
				}
				List<ProductSpec> ps = productSpecDao.findByProductProductId(pDto.getProductId());
				List<ProductSpecDto> psDtos = ps.stream().map(spec -> {
					ProductSpecDto specDto = new ProductSpecDto();
					BeanUtils.copyProperties(spec, specDto);
					return specDto;
				}).collect(Collectors.toList());
				pDto.setProductSpecDtos(psDtos);
				return pDto;
			}).collect(Collectors.toList());
			int totalPage = productPage.getTotalPages();
			productDtos.forEach(pDto -> pDto.setTotalPage(totalPage));
			return productDtos;
		}
	}

	// 登入
	public MemberDto login(String email, String password) {
		// 根據帳密取得物件
		Member member = memberDao.findByMemberEmailAndMemberPasswordAndAccountStatusNot(email, password, "0");
		// 不存在return空，表示登入失敗
		if (member == null) {
			return null;
		}
		// 存在建立Dto，儲存登入資訊
		MemberDto memberDto = new MemberDto();
		BeanUtils.copyProperties(member, memberDto);
		byte[] memberPhoto = member.getMemberDetail().getMemberPhoto();
		if (memberPhoto != null) {
			String mimetype = "image/*";
			try {
				mimetype = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(memberPhoto));
			} catch (IOException e) {
				e.printStackTrace();
			}
			String base64 = Base64.getEncoder().encodeToString(memberPhoto);
			memberDto.setMemberPhoto("data:%s;base64,%s".formatted(mimetype, base64));
		}
		Member m = memberDao.findByMemberEmail(email);
		Date d = new Date();
		m.setLastLoginTime(d);
		memberDao.save(m);
		return memberDto;
	}

	// 註冊會員
	public boolean register(String name, String email, String password) {
		Member member = memberDao.findByMemberEmail(email);
		Member m = new Member();
		MemberDetail md = new MemberDetail();
		if (member == null) {
			m.setMemberName(name);
			m.setMemberEmail(email);
			m.setMemberPassword(password);
			m.setAccountStatus("1");
			Date d = new Date();
			m.setCreateTime(d);
			m.setLastLoginTime(d);
			m.setMemberDetail(md);
			md.setMember(m);
			memberDao.save(m);
			return true;
		}
		return false;
	}

	// 新增商品分類
	public boolean addProductCategory(ProductCategory productCategory, String parentId) {
		ProductCategory pc = new ProductCategory();
		BeanUtils.copyProperties(productCategory, pc);
		Date d = new Date();
		pc.setCreateTime(d);
		pc.setUpdateTime(d);
		if (parentId != null && !parentId.isEmpty() && !parentId.equalsIgnoreCase("null")) {
			pc.setParentCategory(productCategoryDao.findByProductCategoryId(Integer.parseInt(parentId)));
		}
		productCategoryDao.save(pc);
		return true;
	}

	// 顯示商品分類
	public List<ProductCategory> findAll() {
		return productCategoryDao.findAll();
	}

	// 顯示商品分類
	public List<ProductCategoryDto> findProductCategory(Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 10);
		Page<ProductCategory> productCategoryPage = productCategoryDao.findAll(pageable);
		List<ProductCategoryDto> productCategoryDtos = productCategoryPage.getContent().stream().map(pc -> {
			ProductCategoryDto pcDto = new ProductCategoryDto();
			BeanUtils.copyProperties(pc, pcDto);
			int totalPage = productCategoryPage.getTotalPages();
			pcDto.setTotalPage(totalPage);
			if (pc.getParentCategory() != null) {
				pcDto.setParentCategoryId(pc.getParentCategory().getProductCategoryId());
				pcDto.setParentCategoryName(pc.getParentCategory().getCategoryName());
			}
			return pcDto;
		}).collect(Collectors.toList());
		return productCategoryDtos;
	}

	// 修改商品分類
	public ProductCategory updateProductCategory(ProductCategoryDto pcDto) {
		ProductCategory pc = productCategoryDao.findByProductCategoryId(pcDto.getProductCategoryId());
		pc.setCategoryName(pcDto.getCategoryName());
		pc.setCategoryDescription(pcDto.getCategoryDescription());
		pc.setSortOrder(pcDto.getSortOrder());
		Date a = new Date();
		pc.setUpdateTime(a);
		productCategoryDao.save(pc);
		return pc;
	}

	// 刪除商品分類
	public boolean deleteProductCategory(String productCategoryId) {
		ProductCategory pc = productCategoryDao.findByProductCategoryId(Integer.valueOf(productCategoryId));

		// 檢查是否有任何子類別
		List<ProductCategory> childCategories = productCategoryDao.findByParentCategory(pc);
		if (!childCategories.isEmpty()) {
			// 如果存在子類別，表示該類別不是最底層類別，無法刪除
			return false;
		} else {
			// 如果不存在子類別，可以刪除
			productCategoryDao.delete(pc);
			return true;
		}
	}

	// 新增商品
	public boolean addProduct(ProductDto productDto, List<ProductSpecDto> productSpecs, byte[] productPhoto,
			Integer memberId) {
		Product p = new Product();
		BeanUtils.copyProperties(productDto, p);
		Date d = new Date();
		p.setCreateTime(d);
		p.setUpdateTime(d);
		p.setMemberId(memberId);
		p.setProductStatus("1");
		productDao.save(p);
		ProductPhoto pp = new ProductPhoto();
		pp.setProductPhoto(productPhoto);
		pp.setSortOrder(1);
		pp.setProduct(p);
		productPhotoDao.save(pp);
		for (ProductSpecDto psDto : productSpecs) {
			ProductSpec ps = new ProductSpec();
			ps.setProductSpecName(psDto.getProductSpecName());
			ps.setStockQuantity(psDto.getStockQuantity());
			ps.setProductOriginPrice(psDto.getProductOriginPrice());
			ps.setProductSellingPrice(psDto.getProductSellingPrice());
			ps.setProduct(p);
			productSpecDao.save(ps);
		}
		return true;
	}

	// 修改賣家商品
	public boolean updateProductByProductId(String productDataJson, String deleteProductSpecIdJson,
			String productSpecsJson, MultipartFile productPhoto) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ProductDto productDto = objectMapper.readValue(productDataJson, ProductDto.class);
		Product p = productDao.findByProductId(productDto.getProductId());
		p.setProductDescription(productDto.getProductDescription());
		p.setProductName(productDto.getProductName());
		p.setProductCategoryId(productDto.getProductCategoryId());
		Date d = new Date();
		p.setUpdateTime(d);
		productDao.save(p);
		List<String> deleteProductSpecIds = objectMapper.readValue(deleteProductSpecIdJson,
				new TypeReference<List<String>>() {
				});
		for (String deleteProductSpecId : deleteProductSpecIds) {
			ProductSpec ps = productSpecDao.findByProductSpecId(Integer.valueOf(deleteProductSpecId));
			productSpecDao.delete(ps);
		}
		List<ProductSpecDto> productSpecs = objectMapper.readValue(productSpecsJson,
				new TypeReference<List<ProductSpecDto>>() {
				});
		for (ProductSpecDto ps : productSpecs) {
			if (ps.getProductSpecId() != null) {
				ProductSpec updateSpec = productSpecDao.findByProductSpecId(ps.getProductSpecId());
				BeanUtils.copyProperties(ps, updateSpec);
				productSpecDao.save(updateSpec);
			}
			if (ps.getProductSpecId() == null) {
				ProductSpec newSpec = new ProductSpec();
				BeanUtils.copyProperties(ps, newSpec);
				newSpec.setProduct(p);
				productSpecDao.save(newSpec);
			}
		}
		ProductPhoto pp = productPhotoDao.findByProduct(p);
		if (productPhoto != null) {
			byte[] productPicture = productPhoto.getBytes();
			pp.setProductPhoto(productPicture);
			productPhotoDao.save(pp);
		}
		return true;
	}

	// 修改賣家商品-上下架切換
	public boolean updateProductStatusByProductId(Integer productId) {
		Product p = productDao.findByProductId(productId);
		if ("1".equals(p.getProductStatus())) {
			p.setProductStatus("0");
			productDao.save(p);
		} else if ("0".equals(p.getProductStatus())) {
			p.setProductStatus("1");
			productDao.save(p);
		}
		return true;
	}

	// 賣家商品下架 狀態改9
	public boolean deleteProductStatusByProductId(Integer productId) {
		Product p = productDao.findByProductId(productId);
		if (!"9".equals(p.getProductStatus())) {
			p.setProductStatus("9");
			List<ProductSpec> ps = productSpecDao.findByProduct(p);
			productSpecDao.deleteAll(ps);
			productDao.save(p);

		} else if ("9".equals(p.getProductStatus())) {
			p.setProductStatus("1");
			productDao.save(p);
		}
		return true;
	}

	// 購物車商品-加
	public ShoppingCartItem plusProductToCart(Integer memberId, Integer productSpecId) {
		Member m = new Member(memberId);
		ProductSpec ps = new ProductSpec(productSpecId);
		ShoppingCartItem sc = shoppingCartItemDao.findByMemberAndProductSpec(m, ps);
		if (sc != null) {
			sc.setQuantity(sc.getQuantity() + 1);
		}
		if (sc == null) {
			sc = new ShoppingCartItem();
			sc.setMember(m);
			sc.setProductSpec(ps);
			ProductSpec p = productSpecDao.findByProductSpecId(productSpecId);
			if (p == null) {
				return null;
			}
			sc.setProduct(p.getProduct());
			sc.setQuantity(1);
		}
		@SuppressWarnings("unused")
		ShoppingCartItem shoppingCartItem = shoppingCartItemDao.save(sc);
		return sc;
	}

	// 購物車商品-減
	public ShoppingCartItem minusProductToCart(Integer memberId, Integer productSpecId) {
		Member m = new Member(memberId);
		ProductSpec ps = new ProductSpec(productSpecId);
		ShoppingCartItem sc = shoppingCartItemDao.findByMemberAndProductSpec(m, ps);
		if (sc != null && sc.getQuantity() > 1) {
			sc.setQuantity(sc.getQuantity() - 1);
			@SuppressWarnings("unused")
			ShoppingCartItem shoppingCartItem = shoppingCartItemDao.save(sc);
		} else if (sc != null && sc.getQuantity() == 1) {
			shoppingCartItemDao.deleteById(sc.getCartItemId());
		} else if (sc == null) {
			return null;
		}
		return sc;
	}

	// 購物車商品-導向購物車時
	public ShoppingCartItem createProductToCart(Integer memberId, Integer productSpecId, Integer quantity) {
		Member m = new Member(memberId);
		ProductSpec ps = new ProductSpec(productSpecId);
		ShoppingCartItem sc = shoppingCartItemDao.findByMemberAndProductSpec(m, ps);
		if (sc != null) {
			sc.setQuantity(sc.getQuantity() + quantity);
		}
		if (sc == null) {
			sc = new ShoppingCartItem();
			sc.setMember(m);
			sc.setProductSpec(ps);
			ProductSpec p = productSpecDao.findByProductSpecId(productSpecId);
			if (p == null) {
				return null;
			}
			sc.setProduct(p.getProduct());
			sc.setQuantity(quantity);
		}
		@SuppressWarnings("unused")
		ShoppingCartItem shoppingCartItem = shoppingCartItemDao.save(sc);
		return sc;
	}

	// 購物車商品-刪除規格
	public ShoppingCartItem deleteProductToCart(Integer memberId, Integer productSpecId) {
		Member m = new Member(memberId);
		ProductSpec ps = new ProductSpec(productSpecId);
		ShoppingCartItem sc = shoppingCartItemDao.findByMemberAndProductSpec(m, ps);
		if (sc != null) {
			shoppingCartItemDao.deleteById(sc.getCartItemId());
			return sc; // 返回成功刪除的購物車項目對象
		} else {
			throw new RuntimeException("找不到要刪除的購物車商品"); // 或者根據情況返回其他適當的錯誤消息
		}
	}

	// 顯示購物車
	public List<ShoppingCartItemDto> findShoppingCartByMemberId(Integer memberId) {
		List<ShoppingCartItem> shoppingCartItems = shoppingCartItemDao.findByMember(new Member(memberId));
		List<ShoppingCartItemDto> shoppingCartItemDtos = shoppingCartItems.stream().map(c -> {
			ShoppingCartItemDto sc = new ShoppingCartItemDto();
			Product p = c.getProduct();
			ProductSpec ps = c.getProductSpec();
			BeanUtils.copyProperties(p, sc);
			BeanUtils.copyProperties(ps, sc);
			BeanUtils.copyProperties(c, sc);
			List<ProductPhoto> productPhotos = p.getProductPhotos();
			if (productPhotos != null && productPhotos.size() != 0) {
				ProductPhoto firstPhoto = productPhotos.get(0);
				sc.setProductPhotoId(firstPhoto.getProductPhotoId());
			}
			return sc;
		}).toList();
		return shoppingCartItemDtos;

	}

	// 新增訂單
	public String addOrder(MemberOrderDto memberOrderDto, List<MemberOrderDetailDto> memberOrderDetailDtoList,
			Integer memberId) {
		MemberOrder mo = new MemberOrder();
		BeanUtils.copyProperties(memberOrderDto, mo);
		String paymentMethodId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		mo.setPaymentMethodId(paymentMethodId);
		Date d = new Date();
		mo.setOrderDate(d);
		mo.setOrderStatus("待付款");
		mo.setMember(memberDao.findByMemberId(memberId));

		List<MemberOrderDetail> mods = memberOrderDetailDtoList.stream().map(memberOrderDetailDto -> {
			MemberOrderDetail mod = new MemberOrderDetail();
			BeanUtils.copyProperties(memberOrderDetailDto, mod);
			mod.setMemberOrder(mo);
			mod.setProduct(productDao.findByProductId(memberOrderDetailDto.getProductId()));
			mod.setProductSpecId(memberOrderDetailDto.getProductSpecId());
			ProductSpec ps = productSpecDao.findByProductSpecId(memberOrderDetailDto.getProductSpecId());
			mod.setProductSpecName(ps.getProductSpecName());
			mo.setSellerId(mod.getProduct().getMemberId());
			return mod;
		}).collect(Collectors.toList());
		memberOrderDao.save(mo);
		memberOrderDetailDao.saveAll(mods);
		return paymentMethodId;
	}

	// 賣家買家訂單查詢
	public List<MemberOrderDto> findMemberOrder(Integer memberId, Integer mode, String searchText, Integer pageNumber) {
		Page<MemberOrder> memberOrders = new PageImpl<>(new ArrayList<>());
		// 買家
		if (mode > 0 && mode < 10) {
			Pageable pageable = PageRequest.of(pageNumber, 10);
			if (mode == 1) {
				memberOrders = memberOrderDao.findByMemberMemberIdOrderByOrderDateDesc(memberId, pageable);
			} else if (mode == 2) {
				memberOrders = memberOrderDao.findByMemberMemberIdAndOrderStatusOrderByOrderDateDesc(memberId, "待付款",
						pageable);
			} else if (mode == 3) {
				memberOrders = memberOrderDao.findByMemberMemberIdAndOrderStatusOrderByOrderDateDesc(memberId, "待出貨",
						pageable);
			} else if (mode == 4) {
				memberOrders = memberOrderDao.findByMemberMemberIdAndOrderStatusOrderByOrderDateDesc(memberId, "運送中",
						pageable);
			} else if (mode == 5) {
				memberOrders = memberOrderDao.findByMemberMemberIdAndOrderStatusOrderByOrderDateDesc(memberId, "已完成",
						pageable);
			} else if (mode == 6) {
				memberOrders = memberOrderDao.findByMemberMemberIdAndOrderStatusOrderByOrderDateDesc(memberId, "不成立",
						pageable);
			} else if (mode == 7) {
				memberOrders = memberOrderDao.findByMemberMemberIdAndOrderStatusOrderByOrderDateDesc(memberId, "退貨退款",
						pageable);
			}
		}
		// 賣家
		if (mode > 10) {
			Pageable pageable = PageRequest.of(pageNumber, 12);
			// all
			if (mode == 11) {
				memberOrders = memberOrderDao.findBySellerIdOrderByOrderDateDesc(memberId, pageable);
			} else if (mode == 12) {
				memberOrders = memberOrderDao.findBySellerIdAndOrderStatusOrderByOrderDateDesc(memberId, "待付款",
						pageable);
			} else if (mode == 13) {
				memberOrders = memberOrderDao.findBySellerIdAndOrderStatusOrderByOrderDateDesc(memberId, "待出貨",
						pageable);
			} else if (mode == 14) {
				memberOrders = memberOrderDao.findBySellerIdAndOrderStatusOrderByOrderDateDesc(memberId, "運送中",
						pageable);
			} else if (mode == 15) {
				memberOrders = memberOrderDao.findBySellerIdAndOrderStatusOrderByOrderDateDesc(memberId, "已完成",
						pageable);
			} else if (mode == 16) {
				memberOrders = memberOrderDao.findBySellerIdAndOrderStatusOrderByOrderDateDesc(memberId, "不成立",
						pageable);
			} else if (mode == 17) {
				memberOrders = memberOrderDao.findBySellerIdAndOrderStatusOrderByOrderDateDesc(memberId, "退貨退款",
						pageable);
			}
			// MemberOrderId
			if (mode == 21) {
				memberOrders = memberOrderDao.findBySellerIdAndMemberOrderIdOrderByOrderDateDesc(memberId,
						Integer.parseInt(searchText), pageable);
			} else if (mode == 22) {
				memberOrders = memberOrderDao.findBySellerIdAndMemberOrderIdAndOrderStatusOrderByOrderDateDesc(memberId,
						Integer.parseInt(searchText), "待付款", pageable);
			} else if (mode == 23) {
				memberOrders = memberOrderDao.findBySellerIdAndMemberOrderIdAndOrderStatusOrderByOrderDateDesc(memberId,
						Integer.parseInt(searchText), "待出貨", pageable);
			} else if (mode == 24) {
				memberOrders = memberOrderDao.findBySellerIdAndMemberOrderIdAndOrderStatusOrderByOrderDateDesc(memberId,
						Integer.parseInt(searchText), "運送中", pageable);
			} else if (mode == 25) {
				memberOrders = memberOrderDao.findBySellerIdAndMemberOrderIdAndOrderStatusOrderByOrderDateDesc(memberId,
						Integer.parseInt(searchText), "已完成", pageable);
			} else if (mode == 26) {
				memberOrders = memberOrderDao.findBySellerIdAndMemberOrderIdAndOrderStatusOrderByOrderDateDesc(memberId,
						Integer.parseInt(searchText), "不成立", pageable);
			} else if (mode == 27) {
				memberOrders = memberOrderDao.findBySellerIdAndMemberOrderIdAndOrderStatusOrderByOrderDateDesc(memberId,
						Integer.parseInt(searchText), "退貨退款", pageable);
			}
			// MemberOrderId
			if (mode == 31) {
				memberOrders = memberOrderDao.findBySellerIdAndMemberMemberNameContainingOrderByOrderDateDesc(memberId,
						searchText, pageable);
			} else if (mode == 32) {
				memberOrders = memberOrderDao
						.findBySellerIdAndMemberMemberNameContainingAndOrderStatusOrderByOrderDateDesc(memberId,
								searchText, "待付款", pageable);
			} else if (mode == 33) {
				memberOrders = memberOrderDao
						.findBySellerIdAndMemberMemberNameContainingAndOrderStatusOrderByOrderDateDesc(memberId,
								searchText, "待出貨", pageable);
			} else if (mode == 34) {
				memberOrders = memberOrderDao
						.findBySellerIdAndMemberMemberNameContainingAndOrderStatusOrderByOrderDateDesc(memberId,
								searchText, "運送中", pageable);
			} else if (mode == 35) {
				memberOrders = memberOrderDao
						.findBySellerIdAndMemberMemberNameContainingAndOrderStatusOrderByOrderDateDesc(memberId,
								searchText, "已完成", pageable);
			} else if (mode == 36) {
				memberOrders = memberOrderDao
						.findBySellerIdAndMemberMemberNameContainingAndOrderStatusOrderByOrderDateDesc(memberId,
								searchText, "不成立", pageable);
			} else if (mode == 37) {
				memberOrders = memberOrderDao
						.findBySellerIdAndMemberMemberNameContainingAndOrderStatusOrderByOrderDateDesc(memberId,
								searchText, "退貨退款", pageable);
			}
		}
		List<MemberOrderDto> moDtos = new ArrayList<>();
		for (MemberOrder memberOrder : memberOrders) {
			List<MemberOrderDetailDto> modDtos = new ArrayList<>();
			MemberOrderDto moDto = new MemberOrderDto();
			BeanUtils.copyProperties(memberOrder, moDto);
			int totalPage = memberOrders.getTotalPages();
			moDto.setTotalPage(totalPage);
			moDto.setMemberId(memberOrder.getMember().getMemberId());
			moDto.setMemberName(memberOrder.getMember().getMemberName());
			moDto.setDeliveryFee(memberOrder.getDeliveryFee());
			List<MemberOrderDetail> mods = memberOrder.getMemberOrderDetails();
			for (MemberOrderDetail memberOrderDetail : mods) {
				MemberOrderDetailDto modDto = new MemberOrderDetailDto();
				BeanUtils.copyProperties(memberOrderDetail, modDto);
				modDto.setMemberOrderId(memberOrderDetail.getMemberOrder().getMemberOrderId());
				modDto.setProductId(memberOrderDetail.getProduct().getProductId());
				modDto.setProductSpecId(memberOrderDetail.getProductSpecId());
				modDto.setProductSpecName(memberOrderDetail.getProductSpecName());
				modDtos.add(modDto);
			}
			moDto.setMemberOrderDetails(modDtos);
			moDtos.add(moDto);
		}
		return moDtos;
	}

	// 修改訂單狀態
	public boolean updateMemberOrderByMemberIdToCancelled(String orderStatus, Integer memberOrderId) {
		MemberOrder mo = memberOrderDao.findByMemberOrderId(memberOrderId);
		if (orderStatus.equals("待付款Cancel")) {
			mo.setOrderStatus("不成立");
		} else if (orderStatus.equals("待出貨Cancel") || orderStatus.equals("運送中Cancel")) {
			mo.setOrderStatus("退貨退款");
			// 退款
			createTrade(mo.getMember().getMemberId(), mo.getTotalAmount());
		} else if (orderStatus.equals("運送中Complete")) {
			mo.setOrderStatus("已完成");
		}
		memberOrderDao.save(mo);
		return true;
	}

	// 更新評價
	public boolean updateOrderComment(List<MemberOrderDetailDto> modDtos) {
		for (MemberOrderDetailDto modDto : modDtos) {
			MemberOrderDetail mod = memberOrderDetailDao.findByOrderDetailId(modDto.getOrderDetailId());
			if (mod != null) {
				mod.setStar(modDto.getStar());
				mod.setComment(modDto.getComment());
				memberOrderDetailDao.save(mod);
			} else {
				return false;
			}
		}
		return true;
	}

	// 商品頁面展示評論(會員id、會員名稱、購買規格、評價星級、評論內容)
	public List<CommentDto> memberCommemt(Integer productId) {
		List<CommentDto> commemtDtos = new ArrayList<>();
		// 尋找該產品所包含的spec
		List<ProductSpec> specs = productSpecDao.findByProductProductId(productId);
		for (ProductSpec spec : specs) {
			List<MemberOrderDetail> mods = memberOrderDetailDao
					.findByProductSpecIdAndStarNotNull(spec.getProductSpecId());
			for (MemberOrderDetail mod : mods) {
				MemberOrder mos = memberOrderDao.findByMemberOrderId(mod.getMemberOrder().getMemberOrderId());
				CommentDto commemtDto = new CommentDto();
				commemtDto.setMemberOrderId(mos.getMemberOrderId());
				commemtDto.setMemberId(mos.getMember().getMemberId());
				commemtDto.setMemberName(mos.getMember().getMemberName());
				commemtDto.setProductSpecName(mod.getProductSpecName());
				if (mod.getStar() != null) {
					commemtDto.setStar(mod.getStar());
				}
				if (mod.getComment() != null) {
					commemtDto.setComment(mod.getComment());
				}
				commemtDtos.add(commemtDto);
			}
		}
		return commemtDtos;
	}

	// 賣家訂單狀態修改
	public boolean updateMemberOrderByMemberId(Integer memberOrderId, String trackingNumber) throws IOException {
		MemberOrder mo = memberOrderDao.findByMemberOrderId(memberOrderId);
		if (mo.getOrderStatus().equals("待出貨")) {
			mo.setOrderStatus("運送中");
			mo.setDeliveryMethod(mo.getDeliveryMethod() + "(" + trackingNumber + ")");
			memberOrderDao.save(mo);
			return true;
		} else {
			return false;
		}

	}

	// 網址下載照片
	public byte[] downloadImage(String imageUrl) throws IOException {
		URL url = new URL(imageUrl);
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); InputStream is = url.openStream()) {
			byte[] byteChunk = new byte[4096]; // 設定緩衝區大小
			int n;
			while ((n = is.read(byteChunk)) > 0) {
				baos.write(byteChunk, 0, n);
			}
			return baos.toByteArray();
		}
	}

	// pay
	public String pay(String uuid) {
		MemberOrder mo = memberOrderDao.findByPaymentMethodId(uuid);
		if (mo.getPaymentMethod().equals("ecpay")) {
			// 一般信用卡測試卡號 : 4311-9522-2222-2222
			// 安全碼 : 222
			// 開始產生與獲取綠界所需必要資料(特店訂單編號、特店交易時間、交易金額、交易描述、商品名稱、ReturnURL(回傳交易結果給商家的網址)、OrderResultURL(選填)(消費者完成付費後。重新導向的位置))
			AllInOne all = new AllInOne("");
			AioCheckOutALL obj = new AioCheckOutALL();
			// 隱藏除了信用卡外的付款方式
			// obj.setIgnorePayment("AndroidPay#WebATM#ATM#CVS#BARCODE#TWQR#BNPL");
			// 訂單編號(採用uuid自動生成)(不同於訂單編號，此編號本應由後台持有之訂單編號加上雜湊值產生，此變數不儲存於資料庫，而是記錄在綠界後台，如果送出訂單實此變數與綠界後台紀錄有相同，則成立訂單失敗)
			obj.setMerchantTradeNo(uuid);
			// 交易時間(按系統時間成立)
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/d HH:mm:ss");
			obj.setMerchantTradeDate(sdf.format(mo.getOrderDate()));
			// 交易總金額(須為整數，不能為小數點)
			obj.setTotalAmount(mo.getTotalAmount().toString());
			// 交易描述(不能有特殊字元)
			obj.setTradeDesc("感謝您的購買，LetItGo購物網竭誠歡迎您的下次惠顧");
			// 商品名稱(如果商品名稱有多筆，需在金流選擇頁一行一行顯示商品名稱的話，商品名稱請以符號#分隔。另，商品名稱字數限制為中英數400字內，超過此限制系統將自動截斷。)
			obj.setItemName("請到我的訂單查看");
			// 付款完成通知回傳網址(ReturnURL為付款結果通知回傳網址，為特店server或主機的URL，用來接收綠界後端回傳的付款結果通知，但因為專案沒上線，所以目前暫無)
			// ReturnURL收到綠界後端回傳的付款結果通知後，請回應字串1|OK給綠界。
			obj.setReturnURL("temp");
			// OrderResultURL : 選填 消費者完成付費後。重新導向的位置(如果有此變數則以此為先，如沒有最後會停在綠界付款介面)
			// obj.setOrderResultURL("http://localhost:5173/");
			// 額外的付款資訊(若不回傳額外的付款資訊時，參數值請傳：Ｎ)
			obj.setNeedExtraPaidInfo("N");
			// 付款完成後可跳轉回首頁
			obj.setClientBackURL("http://localhost:5173/order");
			// 產生綠界form表單
			String form = all.aioCheckOut(obj, null);
			mo.setOrderStatus("待出貨");
			memberOrderDao.save(mo);
			// 於console區列印出表單資訊
			System.err.println(form);
			return form;

		} else if (mo.getPaymentMethod().equals("LinePay")) {
			String storeName = "LetItGo購物";
			String paymentUrl = LinePayService.consumerCheck(mo.getTotalAmount(), uuid, storeName,
					mo.getDeliveryName() + "的訂單");
			if (paymentUrl != null) {
				return paymentUrl; // 導向Line Pay付款頁面
			} else {
				String errorUrl = "http://localhost:5173/linepay/error"; // 導向付款失敗頁面
				return errorUrl;
			}
		}
		return null;
	}

	// linepay
	public ModelAndView confirmApi(@RequestParam String transactionId, @RequestParam String orderId) {
		MemberOrder memberOrder = memberOrderDao.findByPaymentMethodId(orderId);
		ModelAndView modelAndView = new ModelAndView();
		if (memberOrder != null) {
			int totalAmount = memberOrder.getTotalAmount();
			// 執行Service
			LinePayService.confirmApi(totalAmount, transactionId, orderId);
			// 將資料庫訂單設為待出貨
			memberOrder.setOrderStatus("待出貨"); // 設為待出貨
			memberOrderDao.save(memberOrder);

			String ordersUrl = "http://localhost:5173/order";
			modelAndView.setViewName("redirect:" + ordersUrl); // 導向至買家訂單頁面
		} else {
			String errorUrl = "http://localhost:5173/linepay/error";
			modelAndView.setViewName("redirect:" + errorUrl); // 導向付款失敗頁面
		}
		return modelAndView; // 重新導向
	}

	// 會員錢包
	public List<TradeDto> findTradeByMemberId(Integer memberId, Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 12);
		Page<Trade> trades = tradeDao.findByMemberIdOrderByUpdateTimeDesc(memberId, pageable);
		List<TradeDto> tDtos = new ArrayList<>();
		for (Trade trade : trades) {
			TradeDto tDto = new TradeDto();
			BeanUtils.copyProperties(trade, tDto);
			int totalPage = trades.getTotalPages();
			tDto.setTotalPage(totalPage);
			tDtos.add(tDto);
		}
		return tDtos;
	}

	public void submitReport(Report report) {
		report.setCreateTime(new Date());
		report.setReportStatus("待處理");
		reportDao.save(report);
	}

	// 顯示所有 report
	public List<ReportDto> findAllReportsSortedByCreateTimeDesc(Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createTime"));
		Page<Report> reportPage = reportDao.findAll(pageable);
		List<ReportDto> reportDtos = reportPage.getContent().stream().map(r -> {
			ReportDto rDto = new ReportDto();
			BeanUtils.copyProperties(r, rDto);
			int totalPage = reportPage.getTotalPages();
			rDto.setTotalPage(totalPage);
			return rDto;
		}).collect(Collectors.toList());
		return reportDtos;
	}

	// 根據 report 內容修改 report 狀態(成立)
	public boolean changeReportYes(ReportDto rd) {
		Product p = productDao.findByProductId(rd.getProductId());
		if (p != null) {
			p.setProductStatus("8");
			productDao.save(p);
		}
		Report r = reportDao.findByReportId(rd.getReportId());
		if (r != null) {
			r.setReportStatus("已成立");
			reportDao.save(r);
		}
		return true;
	}

	// 根據 report 內容修改 report 狀態(不成立)
	public boolean changeReportNot(ReportDto rd) {
		Report r = reportDao.findByReportId(rd.getReportId());
		if (r != null) {
			r.setReportStatus("不成立");
			reportDao.save(r);
		}
		return true;
	}

	// 所有交易
	public List<TradeDto> getAllTradesSortedByUpdateTimeDesc(Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "updateTime"));
		Page<Trade> tradePage = tradeDao.findAll(pageable);
		List<TradeDto> tradeDtos = tradePage.getContent().stream().map(t -> {
			TradeDto tDto = new TradeDto();
			BeanUtils.copyProperties(t, tDto);
			int totalPage = tradePage.getTotalPages();
			tDto.setTotalPage(totalPage);
			return tDto;
		}).collect(Collectors.toList());
		return tradeDtos;
	}

	// ===============================================================================================

	// 創建新錢包
	public Trade createWallet(Integer memberId) {
		// 檢查是否已存在相同的 memberId
		boolean memberIdExists = tradeDao.existsByMemberId(memberId);
		if (!memberIdExists) {
			Trade newTrade = new Trade();
			newTrade.setMemberId(memberId);
			newTrade.setUpdateTime(new Date());
			newTrade.setChange(0);
			newTrade.setWalletAmount(0);
			newTrade.setStatus(""); // 如果有預設值的話可以填入
			return tradeDao.save(newTrade);
		} else {
			System.err.println("已存在相同memberId，無法新建");
			return null;
		}
	}

	// 依據 memberId 選擇並只顯示最新的資料
	public Trade getLatestTradeByMemberId(Integer memberId) {
		// 根據 memberId 查詢最新的交易，以 updateTime 降序排序，並取第一筆
		List<Trade> trades = tradeDao.findByMemberIdOrderByUpdateTimeDesc(memberId);
		if (!trades.isEmpty()) {
			return trades.get(0);
		} else {
			return createWallet(memberId);
		}
	}

	// 新增一筆交易
	public Trade createTrade(Integer memberId, Integer change) {
		// 根據 memberId 查詢最新的交易
		Trade latestTrade = getLatestTradeByMemberId(memberId);
		if (latestTrade != null) {
			// 獲取最新交易的 walletAmount
			Integer walletAmount = latestTrade.getWalletAmount();
			// 計算新的 walletAmount
			Integer newWalletAmount = walletAmount + change;
			// 創建新的交易
			Trade newTrade = new Trade();
			newTrade.setMemberId(memberId);
			newTrade.setUpdateTime(new Date());
			newTrade.setChange(change);
			newTrade.setWalletAmount(newWalletAmount);
			if (change > 0) {
				newTrade.setStatus("轉入錢包"); // 如果有預設值的話可以填入
			} else if (change < 0) {
				newTrade.setStatus("已申請撥款");
			} else {
				newTrade.setStatus(latestTrade.getStatus());
			}
			// 插入到資料庫
			return tradeDao.save(newTrade);
		} else {
			// 如果找不到最新的交易
			// 創建新的交易
			Trade newTrade = new Trade();
			newTrade.setMemberId(memberId);
			newTrade.setUpdateTime(new Date());
			newTrade.setChange(change);
			newTrade.setWalletAmount(change);
			if (change > 0) {
				newTrade.setStatus("轉入錢包"); // 如果有預設值的話可以填入
			} else if (change < 0) {
				newTrade.setStatus("已申請撥款");
			} else {
				newTrade.setStatus("");
			}
			// 插入到資料庫
			return tradeDao.save(newTrade);
		}
	}

	public void agreeTrade(Integer tradeId) {
		Trade trade = tradeDao.findTradeByTradeId(tradeId);

		if (trade == null) {
			System.err.println("異常");
		}
		Trade latestTrade = getLatestTradeByMemberId(trade.getMemberId());
		trade.setStatus("已申請撥款(已處理)");
		System.err.println(tradeId);
		tradeDao.save(trade);

		Trade newTrade = new Trade();
		newTrade.setMemberId(trade.getMemberId());
		newTrade.setUpdateTime(new Date());
		newTrade.setChange(0);
		newTrade.setWalletAmount(latestTrade.getWalletAmount());
		newTrade.setStatus("撥款成功");
		tradeDao.save(newTrade);
	}

	// 將訂單狀態設為已完成，並將款項存入賣家錢包
	public void finishMemberOrder(Integer memberOrderId) {
		MemberOrder memberOrder = memberOrderDao.findById(memberOrderId)
				.orElseThrow(() -> new IllegalArgumentException("查無此訂單: " + memberOrderId));
		if (!"已完成".equals(memberOrder.getOrderStatus())) {
			memberOrder.setOrderStatus("已完成");
			memberOrderDao.save(memberOrder);
			// 將款項轉至賣家的錢包
			List<MemberOrderDetail> memberOrderDetails = memberOrder.getMemberOrderDetails();
			Product product = memberOrderDetails.get(0).getProduct();
			createTrade(product.getMemberId(), memberOrder.getTotalAmount());
		} else {
			throw new IllegalStateException("訂單已完成");
		}
	}

}
