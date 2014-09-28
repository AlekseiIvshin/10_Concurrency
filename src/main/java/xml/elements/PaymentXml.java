package xml.elements;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "payment")
public class PaymentXml{

	@XmlElement(required = true, type=PayerXml.class)
	private PayerXml payer;

	@XmlElement(required = true, type=PayeeXml.class)
	private PayeeXml payee;

	@XmlElement(required = true, type=BankXml.class)
	private BankXml bank;

	@XmlElement(required = true)
	private float cash;

	@XmlElement(required = true)
	private Date dateOfPayment;

	@XmlElement(required = true)
	private Date executionDate;

	public PaymentXml() {
	}

	public PayerXml getPayer() {
		return payer;
	}

	public void setPayer(PayerXml payer) {
		this.payer = payer;
	}

	public PayeeXml getPayee() {
		return payee;
	}

	public void setPayee(PayeeXml payee) {
		this.payee = payee;
	}

	public BankXml getBank() {
		return bank;
	}

	public void setBank(BankXml bank) {
		this.bank = bank;
	}

	public float getCash() {
		return cash;
	}

	public void setCash(float cash) {
		this.cash = cash;
	}

	public Date getDateOfPayment() {
		return dateOfPayment;
	}

	public void setDateOfPayment(Date dateOfPayment) {
		this.dateOfPayment = dateOfPayment;
	}

	public Date getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}
}
