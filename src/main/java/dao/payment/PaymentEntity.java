package dao.payment;

import java.math.BigDecimal;
import java.util.Date;

public interface PaymentEntity {
	int getId();

	void setId(int id);

	PaymentMemberImpl getPayer();

	void setPayer(PaymentMemberImpl payer);

	PaymentMemberImpl getPayee();

	void setPayee(PaymentMemberImpl payee);

	BankEnity getBank();

	void setBank(BankEnity bank);

	BigDecimal getCash();

	void setCash(BigDecimal cash);

	Date getDateOfCreate();

	void setDateOfCreate(Date date);

	Date getDateOfExecute();

	void setDateOfExecute(Date date);
}
