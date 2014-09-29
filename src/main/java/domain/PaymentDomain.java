package domain;

import java.util.Date;

import dao.payment.BankEnity;
import dao.payment.ParticipantEntity;

public interface PaymentDomain {
	ParticipantDomain getPayer();
	void setPayer(ParticipantDomain payer);
	ParticipantDomain getPayee();
	void setPayee(ParticipantDomain payee);
	BankDomain getBank();
	void setBank(BankDomain bank);
	float getCash();
	void setCash(float cash);
	Date getDateOfCreate();
	void setDateOfCreate(Date date);
	Date getDateOfExecute();
	void setDateOfExecute(Date date);
}
