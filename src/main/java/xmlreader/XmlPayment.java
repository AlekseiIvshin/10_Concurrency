package xmlreader;

import java.util.Date;

public interface XmlPayment {
	XmlPayer getPayer();
	void setPayer(XmlPayer payer);
	XmlPayee getPayee();
	void setPayee(XmlPayee payee);
	XmlBank getBank();
	void setBank(XmlBank bank);
	float getCash();
	void setCash(float cash);
	Date getDateOfPayment();
	void setDateOfPayment();
	Date getExecutionDate();
	void setExecutionDate();
}
