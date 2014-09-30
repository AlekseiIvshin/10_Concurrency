package domain;

import java.lang.reflect.ParameterizedType;
import java.util.Set;

import common.ParticipantType;

public interface ParticipantDomain {

	public String getSurname();

	public void setSurname(String surname);

	public String getName();

	public void setName(String name);

	public String getPatronymic() ;

	public void setPatronymic(String patronymic);

	public String getAccount();

	public void setAccount(String account);

	public String getOrganizationName();

	public void setOrganizationName(String organizationName);

	public DocumentDomain getDocument();

	public void setDocument(DocumentDomain document);

	public String getPhone();

	public void setPhone(String phone);

	public String getAddress();

	public void setAddress(String address);
}
