package dao.payment;

public interface PaymentMember{
	public String getAccount();

	public void setAccount(String account);

	public String getSurname();

	public void setSurname(String surname);

	public String getName();

	public void setName(String name);

	public String getPatronymic();

	public void setPatronymic(String patronymic);

	public Document getDocument();

	public void setDocument(Document document);

	public String getPhone();

	public void setPhone(String phone);

	public String getAddress();

	public void setAddress(String address);

	public String getOrganizationName();

	public void setOrganizationName(String organizationName);
}
