package domain;

import common.DocumentType;

public interface DocumentDomain {

	String getSeries();
	void setSeries();
	String getNumber();
	void setNumber();
	DocumentType getType();
}
