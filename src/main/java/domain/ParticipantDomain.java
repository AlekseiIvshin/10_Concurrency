package domain;

import java.lang.reflect.ParameterizedType;
import java.util.Set;

import common.ParticipantType;

public interface ParticipantDomain {

	String getAccount();
	void setAccount(String account);
	DocumentDomain getDocument();
	void setDocument(DocumentDomain document);
	Set<String> getPhones();
	void setPhones(Set<String> phones);
	String getAddress();
	void setAddress(String address);
	ParticipantType getType();
}
