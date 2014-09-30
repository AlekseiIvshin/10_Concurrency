package dao.payment;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "paymentparticipant")
public class LegalEntityImpl implements PaymentMember {

	@Id
	@Column(name = "account")
	private String account;

	@Column(name = "organization_name")
	private String organizationName;

	@Embedded
	private Document document;

	@Column(name = "phone")
	private String phone;

	@Column(name = "address")
	private String address;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
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

	@Override
	public String getSurname() {
		return null;
	}

	@Override
	public void setSurname(String surname) {

	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void setName(String name) {

	}

	@Override
	public String getPatronymic() {
		return null;
	}

	@Override
	public void setPatronymic(String patronymic) {

	}

}
