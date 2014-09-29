package dao.payment;

import javax.persistence.Embeddable;

import common.DocumentType;

@Embeddable
public interface Document {

	String getSeries();
	void setSeries();
	String getNumber();
	void setNumber();
	DocumentType getType();
}
