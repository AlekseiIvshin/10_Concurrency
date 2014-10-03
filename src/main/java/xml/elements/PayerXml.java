package xml.elements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "payer")
public class PayerXml{

	@XmlElement(required = true)
	private String account;
	
	@XmlElement(required = true)
	private DocumentXml document;
	
	@XmlElement(required = true)
	private String surname;

	@XmlElement(required = true)
	private String name;

	@XmlElement(required = true)
	private String patronymic;
	
	@XmlElement(required = true)
	private String phone;

	@XmlElement(required = true)
	private String address;

	public PayerXml(){}
	
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}


}
