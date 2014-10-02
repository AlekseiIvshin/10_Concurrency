package xml.elements;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "payment")
public class PaymentXml{

	@XmlElement(required = true)
	private PayerXml payer;

	@XmlElement(required = true)
	private PayeeXml payee;

	@XmlElement(required = true)
	private BankXml bank;

	@XmlElement(required = true)
	private float cash;

	@XmlElement(required = true)
	private Date dateOfCreate;

	@XmlElement(required = true)
	private Date dateOfExecute;

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
