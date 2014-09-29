package dao.payment;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import common.ParticipantType;

@Embeddable
public class PayeeEntiy {
	private String account;
	@Column(name = "payee")
	private String name;
	private Document document;
	private String address;
	private ParticipantType type;
	public PayeeEntiy(){}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public ParticipantType getType() {
		return type;
	}
	public void setType(ParticipantType type) {
		this.type = type;
	}
}
