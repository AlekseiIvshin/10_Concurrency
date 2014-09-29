package domain;

import java.util.Date;

public class PaymentDomainImpl implements PaymentDomain {
	private ParticipantDomain payer;
	private ParticipantDomain payee;
	private BankDomain bank;
	private float cash;
	private Date dateOfCreate;
	private Date dateOfExecute;
	
	public ParticipantDomain getPayer() {
		return payer;
	}
	public void setPayer(ParticipantDomain payer) {
		this.payer = payer;
	}
	public ParticipantDomain getPayee() {
		return payee;
	}
	public void setPayee(ParticipantDomain payee) {
		this.payee = payee;
	}
	public BankDomain getBank() {
		return bank;
	}
	public void setBank(BankDomain bank) {
		this.bank = bank;
	}
	public float getCash() {
		return cash;
	}
	public void setCash(float cash) {
		this.cash = cash;
	}
	public Date getDateOfCreate() {
		return dateOfCreate;
	}
	public void setDateOfCreate(Date dateOfCreate) {
		this.dateOfCreate = dateOfCreate;
	}
	public Date getDateOfExecute() {
		return dateOfExecute;
	}
	public void setDateOfExecute(Date dateOfExecute) {
		this.dateOfExecute = dateOfExecute;
	}

}
