package xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xml.elements.PaymentXml;
import xml.elements.PaymentsXml;

public class JAXBProvider implements XmlProvider{

	
	final static Logger logger = LoggerFactory.getLogger(JAXBParser.class);

	Schema schema;
	Unmarshaller unmarshaller;
	Queue<PaymentXml> data;
	int currentPosition;
	
	public JAXBProvider(Schema schema) throws JAXBException {
		data = new LinkedBlockingQueue<PaymentXml>();
		
		this.schema = schema;
		unmarshaller = JAXBContext.newInstance(
				PaymentsXml.class).createUnmarshaller();
		unmarshaller.setSchema(schema);
	}
	
	@Override
	public void parse(File xml) throws FileNotFoundException, XmlException {
		data.clear();
		PaymentsXml paymentsRoot = null;
		try {
			paymentsRoot = (PaymentsXml) unmarshaller
					.unmarshal(new FileInputStream(xml));
		} catch (FileNotFoundException e) {
			logger.error(
					"XML reading: File not found: " + xml.getName(), e);
			throw e;
		} catch (JAXBException e) {
			logger.error("XML reading: Error in unmarshalling file: "
					+ xml.getName(), e);
			throw new XmlException(e.getMessage());
		}

		if (paymentsRoot != null) {
			for(PaymentXml payment: paymentsRoot.getPayments()){
				data.add(payment);
			}
		}
	}

	@Override
	public PaymentXml getNextPayment() {
		return data.poll();
	}

	
}
