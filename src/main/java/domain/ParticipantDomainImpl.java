package domain;

import java.util.Set;

import common.ParticipantType;

public class ParticipantDomainImpl implements ParticipantDomain {
	
	private String account;
	private DocumentDomain document;
	private Set<String> phones;
	private String address;
	private ParticipantType type;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public DocumentDomain getDocument() {
		return document;
	}

	public void setDocument(DocumentDomain document) {
		this.document = document;
	}

	public Set<String> getPhones() {
		return phones;
	}

	public void setPhones(Set<String> phones) {
		this.phones = phones;
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
