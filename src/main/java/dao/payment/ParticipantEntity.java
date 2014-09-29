package dao.payment;

import common.ParticipantType;

public interface ParticipantEntity {

	String getAccount();

	void setAccount();

	String getName();

	void setName(String name);

	Document getDocument();

	void setDocument(Document document);

	String getPhone();

	void setPhone(String phones);

	String getAddress();

	void setAddress(String address);

	ParticipantType getType();
}
