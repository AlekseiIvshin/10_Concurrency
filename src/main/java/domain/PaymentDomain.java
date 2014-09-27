package domain;

import java.util.Date;

import dao.payment.BankEnity;
import dao.payment.ParticipantEntity;

public interface PaymentDomain {
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
