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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xml.elements.PaymentXml;
import xml.elements.PaymentsXml;

public class JAXBParser implements XmlParser {

	final static Logger logger = LoggerFactory.getLogger(JAXBParser.class);

	Schema schema;
	Unmarshaller unmarshaller;

	public JAXBParser(Schema schema) throws JAXBException {
		this.schema = schema;
		unmarshaller = JAXBContext.newInstance(
				PaymentsXml.class).createUnmarshaller();
		unmarshaller.setSchema(schema);
	}

	public List<PaymentXml> parse(String xmlLocation)
			throws FileNotFoundException, XmlException {
		File f = new File(xmlLocation);
		return parse(f);
	}

	@Override
	public List<PaymentXml> parse(File xmlLocation)
			throws FileNotFoundException, XmlException {

		PaymentsXml paymentsRoot = null;
		try {
			paymentsRoot = (PaymentsXml) unmarshaller
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
			return paymentsRoot.getPayments();
		}
		return null;

	}

}
