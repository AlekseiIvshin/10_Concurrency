package dao.payment;

import java.util.Date;

public interface PaymentEntity {

	ParticipantEntity getPayer();
	void setPayer(ParticipantEntity payer);
	ParticipantEntity getPayee();
	void setPayee(ParticipantEntity payee);
	BankEnity getBank();
	void setBank(BankEnity bank);
	float getCash();
	void setCash(float cash);
	Date getDateOfPayment();
	void setDateOfPayment();
	Date getExecutionDate();
	void setExecutionDate();
}
