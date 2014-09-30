package xml;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import xml.JAXBParser;
import xml.XmlException;
import xml.XmlParser;
import xml.elements.PaymentXml;

public class JAXBParserTest {

	private final static String xmlLocation = "xmlExample.xml";
	private final static String schemaLoc = "src\\test\\resources\\schema1.xsd";
	
	private XmlParser parser;
	
	@Before
	public void setUp(){
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		File schemaFile = new File(schemaLoc);
		Schema schema;
		try {
			schema = factory.newSchema(schemaFile);
		} catch (SAXException e1) {
			System.err.println("Error in schema init");
			return;
		}
		parser = new JAXBParser(schema);
	}

	@Test
	public void testGetPayments() throws FileNotFoundException, XmlException {
		List<PaymentXml> payments = parser.parse(xmlLocation);
		assertNotNull(payments);
		assertNotEquals(payments.size(), 0);
		assertEquals(payments.get(0).getCash(), 100.0f,1.0f);
		assertEquals(payments.get(0).getPayer().getSurname(), "Ivonesyan");
	}

	@Test
	public void testParse() throws FileNotFoundException, XmlException {
		parser.parse(xmlLocation);
	}

}
