package xmlreader;

import java.util.Set;

public interface XmlPayments {

	void setPayments(Set<XmlPayment> payments);
	Set<XmlPayment> getPayments();
}
