package xmlreader;

import java.util.Set;

public interface XmlPayer {
	String getAccount();
	void setAccount();
	String getDocumentNumber();
	void setDocumentNumber(String document);
	Set<String> getPhones();
	void setPhones(Set<String> phones);
	String getAddress();
	void setAddress(String address);
}
