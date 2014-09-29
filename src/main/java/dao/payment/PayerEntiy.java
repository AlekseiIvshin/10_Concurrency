package dao.payment;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import common.ParticipantType;

@Embeddable
public class PayerEntiy {
	@Column(name = "payer")
	private String name;
//	private String account;
//	private Document document;
//	private String address;
//	private ParticipantType type;
	
	public PayerEntiy(){}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
//	public String getAccount() {
//		return account;
//	}
//
//	public void setAccount(String account) {
//		this.account = account;
//	}
//
//
//	public Document getDocument() {
//		return document;
//	}
//
//	public void setDocument(Document document) {
//		this.document = document;
//	}
//
//	public String getAddress() {
//		return address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//	}
//
//	public ParticipantType getType() {
//		return type;
//	}
//
//	public void setType(ParticipantType type) {
//		this.type = type;
//	}
}
