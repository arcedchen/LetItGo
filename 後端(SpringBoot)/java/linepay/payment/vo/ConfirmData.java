package linepay.payment.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConfirmData {

	private BigDecimal amount;
	private String currency;
}
