package xml.elements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "payee")
public class PayeeXml {
	@XmlElement(required = true)
	private String account;

	@XmlElement(required = true)
	private DocumentXml document;

	@XmlElement(required = true)
	private String fullName;

	@XmlElement(required = true)
	private String phone;

	@XmlElement(required = true)
	private String address;

	public PayeeXml() {
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public DocumentXml getDocument() {
		return document;
	}

	public void setDocument(DocumentXml document) {
		this.document = document;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
