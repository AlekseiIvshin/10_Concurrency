package xml.provider;

import java.io.File;
import java.io.FileNotFoundException;

import common.exception.XmlException;
import xml.elements.PaymentXml;

public interface XmlProvider {

	void parse(File xml) throws FileNotFoundException, XmlException;
	PaymentXml getNextPayment();
	void close();
}
