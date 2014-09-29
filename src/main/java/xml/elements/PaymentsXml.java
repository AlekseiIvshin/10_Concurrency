package xml.elements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "payments")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", namespace = "http://www.w3.org/2001/XMLSchema")
public class PaymentsXml {
	
	private List<PaymentXml> payment;

	public PaymentsXml() {
	}

	public void setPayments(List<PaymentXml> payments) {
		this.payment = payments;
	}

	public List<PaymentXml> getPayments() {
		if (payment == null) {
			payment = new ArrayList<PaymentXml>();
		}
		return payment;
	}
}
