package xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import xml.elements.PaymentXml;

public interface XmlParser {

	List<PaymentXml> getPayments();

	void parse(String xmlLocation) throws FileNotFoundException, XmlException;
	void parse(File xmlLocation) throws FileNotFoundException, XmlException;
}
