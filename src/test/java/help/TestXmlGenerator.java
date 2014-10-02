package help;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import xml.elements.BankXml;
import xml.elements.DocumentXml;
import xml.elements.PayeeXml;
import xml.elements.PayerXml;
import xml.elements.PaymentXml;

public class TestXmlGenerator {

	Marshaller marshaller;
	XMLStreamWriter writer;

	public TestXmlGenerator() throws JAXBException {
		marshaller = JAXBContext.newInstance(PaymentXml.class)
				.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
	}

	public void generate(String path, int countInPer, int countOfFiles) {
		System.out.println(path);
		for (int i = 0; i < countOfFiles; i++) {
			File tmpFile = new File(path + File.separator + "file" + i + ".xml");
			if (tmpFile.exists()) {
				tmpFile.delete();
			}
			try {
				tmpFile.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			try {
				generateOneFile(tmpFile, countInPer);
			} catch (FileNotFoundException | XMLStreamException | JAXBException e) {
				e.printStackTrace();
				tmpFile.delete();
			}

		}
	}

	private void generateOneFile(File tmpFile, int countOfPayments)
			throws FileNotFoundException, XMLStreamException, JAXBException {

		try {
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			writer = factory
					.createXMLStreamWriter(new FileOutputStream(tmpFile),"utf-8");

			writer.writeStartDocument("utf-8", "1.0");
			writer.writeStartElement("payments");
			for (int i = 0; i < countOfPayments; i++) {
				marshaller.marshal(getRandomData(), writer);
			}
			writer.writeEndDocument();
			//writer.flush();
		} finally {
			writer.close();
		}

	}

	private PaymentXml getRandomData() {
		Random rnd = new Random();
		PaymentXml payment = new PaymentXml();
		payment.setBank(getRandomBank());
		payment.setPayee(getRandomPayee());
		payment.setPayer(getRandomPayer());
		payment.setCash(rnd.nextFloat());
		payment.setDateOfCreate(new Date(2014, 10, 2, rnd.nextInt(24), rnd
				.nextInt(60), rnd.nextInt(60)));
		payment.setDateOfExecute(new Date(2014, 10, 2, rnd.nextInt(24), rnd
				.nextInt(60), rnd.nextInt(60)));

		return payment;
	}

	private DocumentXml getRandomDocument() {
		Random rnd = new Random();
		DocumentXml documentXml = new DocumentXml();
		documentXml.setNumber("9" + rnd.nextInt(400));
		documentXml.setSeries(rnd.nextInt(400) + "" + rnd.nextInt(400));
		documentXml.setType(rnd.nextInt(2) == 0 ? "PASSPORT" : "INN");

		return documentXml;
	}

	private PayeeXml getRandomPayee() {
		Random rnd = new Random();
		PayeeXml result = new PayeeXml();
		int acc = rnd.nextInt(22);
		result.setAccount("100000000000000000" + (acc < 10 ? "0" + acc : acc));
		result.setAddress("Address " + rnd.nextLong());
		result.setDocument(getRandomDocument());
		result.setName("Name" + rnd.nextInt());
		result.setSurname("Name" + rnd.nextInt());
		result.setPatronymic("Name" + rnd.nextInt());
		result.setPhone(rnd.nextInt() + "");

		return result;
	}

	private PayerXml getRandomPayer() {
		Random rnd = new Random();
		PayerXml result = new PayerXml();
		int acc = rnd.nextInt(22);
		result.setAccount("100000000000000000" + (acc < 10 ? "0" + acc : acc));
		result.setAddress("Address " + rnd.nextLong());
		result.setDocument(getRandomDocument());
		result.setName("Name" + rnd.nextInt());
		result.setSurname("Name" + rnd.nextInt());
		result.setPatronymic("Name" + rnd.nextInt());
		result.setPhone(rnd.nextInt() + "");

		return result;
	}

	private BankXml getRandomBank() {
		Random rnd = new Random();
		BankXml result = new BankXml();

		result.setBIC("" + rnd.nextInt(1000000));
		result.setName("Bank" + rnd.nextInt());
		return result;
	}
}
