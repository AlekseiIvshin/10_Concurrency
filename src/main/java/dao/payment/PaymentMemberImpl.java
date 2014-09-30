package dao.payment;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "paymentparticipant")
public class PaymentMemberImpl {

	@Id 
	@Column(name = "id")
	private int id;
	
    @Column(name = "account", columnDefinition = "VARCHAR(20)", length = 20, updatable = false, nullable = false, insertable = false,unique=true)
	private String account;

	@Column(name = "surname")
	private String surname;

	@Column(name = "name")
	private String name;

	@Column(name = "patronymic")
	private String patronymic;
	
	@Column(name = "organization_name")
	private String organizationName;

	@Embedded
	private Document document;

	@Column(name = "phone")
	private String phone;

	@Column(name = "address")
	private String address;


	@Override
	public int hashCode() {
		return account.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PaymentMemberImpl)) {
			return false;
		}
		PaymentMemberImpl other = (PaymentMemberImpl) obj;
		return getAccount().equals(other.getAccount());
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
