package xmlreader;

import common.DocumentType;

public interface XmlDocument {

	String getSeries();
	void setSeries();
	String getNumber();
	void setNumber();
	DocumentType getType();
}
