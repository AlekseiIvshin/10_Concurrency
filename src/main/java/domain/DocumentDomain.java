package domain;

import common.DocumentType;

public interface DocumentDomain {

	String getSeries();
	void setSeries(String number);
	String getNumber();
	void setNumber(String number);
	String getType();
	void setType(String type);
}
