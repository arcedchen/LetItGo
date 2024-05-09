package linepay.payment.vo;

import java.util.List;

import lombok.Data;

@Data
public class CheckoutPaymentRequestForm {
	private int amount;
	private String currency;
	private String orderId;
	private List<ProductPackageForm> packages;
	private RedirectUrls redirectUrls;

}
