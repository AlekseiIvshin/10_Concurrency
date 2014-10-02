package xml.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xml.elements.PaymentXml;
import common.XmlException;

public class JAXBplusStAXProvider implements XmlProvider {

	final static Logger logger = LoggerFactory
			.getLogger(JAXBplusStAXProvider.class);

	private XMLStreamReader xmlReader;
	private Unmarshaller unmarshaller;

	public JAXBplusStAXProvider() throws JAXBException {

		unmarshaller = JAXBContext.newInstance(PaymentXml.class)
				.createUnmarshaller();
	}

	@Override
	public void parse(File xml) throws FileNotFoundException, XmlException {
		if (xmlReader != null) {
			try {
				xmlReader.close();
			} catch (XMLStreamException e) {
				logger.error("Close xml stream reader", e);
				throw new XmlException(e.getMessage());
			}
		}
		XMLInputFactory f = XMLInputFactory.newInstance();
		try {
			xmlReader = f.createXMLStreamReader(new FileInputStream(xml));
		} catch (XMLStreamException e) {
			logger.error("Create xml stream reader", e);
			throw new XmlException(e.getMessage());
		}
	}

	@Override
	public PaymentXml getNextPayment() {
		if (xmlReader == null) {
			return null;
		}
		int eventType = xmlReader.getEventType();
		try {
			while (xmlReader.hasNext()) {
				eventType = xmlReader.next();
				if (eventType == XMLStreamConstants.START_ELEMENT
						&& xmlReader.getLocalName().equals("payment")) {

					try {
						JAXBElement<PaymentXml> jb = unmarshaller.unmarshal(
								xmlReader, PaymentXml.class);
						return jb.getValue();
					} catch (JAXBException e) {
						logger.error("Unmarshall error", e);
						continue;
					}
				}
			}
		} catch (XMLStreamException e) {
			logger.error("XMLStreamException", e);
		}
		return null;
	}

	@Override
	public void close() {
		try {
			xmlReader.close();
		} catch (XMLStreamException e) {
			logger.error("Xml reader close error", e);
		}

	}

}
