package dao.payment;

import common.DocumentType;

public interface Document {

	String getSeries();
	void setSeries();
	String getNumber();
	void setNumber();
	DocumentType getType();
}
