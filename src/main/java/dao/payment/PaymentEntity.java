package dao.payment;

import java.math.BigDecimal;
import java.util.Date;

public interface PaymentEntity {
	long getId();

	void setId(long id);

	PayerEntiy getPayer();

	void setPayer(PayerEntiy payer);

	PayeeEntiy getPayee();

	void setPayee(PayeeEntiy payee);

	BankEnity getBank();

	void setBank(BankEnity bank);

	BigDecimal getCash();

	void setCash(BigDecimal cash);

	Date getDateOfCreate();

	void setDateOfCreate(Date date);

	Date getDateOfExecute();

	void setDateOfExecute(Date date);
}
