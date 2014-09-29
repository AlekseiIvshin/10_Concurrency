package xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

import xml.elements.PaymentXml;
import xml.elements.PaymentsXml;
import xml.elements.PaymentsXml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JAXBParser implements XmlParser {

	final static Logger logger = LoggerFactory.getLogger(JAXBParser.class);

	Schema schema;

	public JAXBParser(Schema schema) {
		this.schema = schema;
	}

	public List<PaymentXml> parse(String xmlLocation)
			throws FileNotFoundException, XmlException {
		File f = new File(xmlLocation);
		return parse(f);
	}

	@Override
	public List<PaymentXml> parse(File xmlLocation)
			throws FileNotFoundException, XmlException {

		List<PaymentXml> payments = new ArrayList<PaymentXml>();
		PaymentsXml paymentsRoot = null;
		try {
			Unmarshaller unmarsheller = JAXBContext.newInstance(
					PaymentsXml.class).createUnmarshaller();
			unmarsheller.setSchema(schema);
			paymentsRoot = (PaymentsXml) unmarsheller
					.unmarshal(new FileInputStream(xmlLocation));
		} catch (FileNotFoundException e) {
			logger.error(
					"XML reading: File not found: " + xmlLocation.getName(), e);
			throw e;
		} catch (JAXBException e) {
			logger.error("XML reading: Error in unmarshalling file: "
					+ xmlLocation.getName(), e);
			throw new XmlException(e.getMessage());
		}

		if (paymentsRoot != null) {
			payments = paymentsRoot.getPayments();
		}
		return payments;

	}

}
