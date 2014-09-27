package dao.payment;

import java.lang.reflect.ParameterizedType;
import java.util.Set;

import common.ParticipantType;

public interface ParticipantEntity {

	String getAccount();
	void setAccount();
	Document getDocument();
	void setDocument(Document document);
	Set<String> getPhones();
	void setPhones(Set<String> phones);
	String getAddress();
	void setAddress(String address);
	ParticipantType getType();
}
