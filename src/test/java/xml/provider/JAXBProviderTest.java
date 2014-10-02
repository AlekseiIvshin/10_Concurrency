package xml.provider;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import common.FactoryException;
import common.SpeedTest;
import common.XmlException;
import xml.elements.PaymentXml;

public class JAXBProviderTest {

	XmlProvider provider;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() {
		try {
			provider = new JAXBProviderFactory().createProvider();
		} catch (FactoryException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testParse() {
		File testXml = new File("C:\\Users\\Aleksei_Ivshin@epam.com\\Documents\\TEST\\xmls\\correct.xml");
		try {
			provider.parse(testXml);
		} catch (FileNotFoundException | XmlException e) {
			fail(e.getMessage());
		}
	}
	

	@Test
	public void testParseCheckData() {
		File testXml = new File("src\\test\\resources\\xmls\\correct.xml");
		try {
			provider.parse(testXml);
		} catch (FileNotFoundException | XmlException e) {
			fail(e.getMessage());
		}
		PaymentXml payment = provider.getNextPayment();
		assertNotNull("Unexpected null payment",payment);
		assertNotNull("Unexpected null in data create", payment.getDateOfCreate());
		assertNotNull("Unexpected null in data execute", payment.getDateOfExecute());
	}

	@Test
	public void testParseNotFileFound() {
		File testXml = new File(
				"C:\\Users\\Aleksei_Ivshin@epam.com\\Documents\\TEST\\xmlExampleNeverWas.xml");

		try {
			provider.parse(testXml);
			fail("File was not found, but was readed!");
		} catch (FileNotFoundException e) {
		} catch (XmlException e) {
			fail("File was not found, but was readed!");
		}
	}

	@Test
	public void testGetNextPaymentFromNotParsed() {
		PaymentXml payment = provider.getNextPayment();
		assertNull(payment);
	}

	@Test
	public void testParseAndGetNextPayment() {
		File testXml = new File("C:\\Users\\Aleksei_Ivshin@epam.com\\Documents\\TEST\\xmls\\correct.xml");
		try {
			provider.parse(testXml);
		} catch (FileNotFoundException | XmlException e) {
			fail(e.getMessage());
			return;
		}
		for (int i = 0; i < 2; i++) {
			PaymentXml payment = provider.getNextPayment();
			assertNotNull("Unexpected payment equals null", payment);
		}
		PaymentXml payment = provider.getNextPayment();
		assertNull("Unexpected payment not equals null", payment);
	}

	@Test
	public void testParseCorruptedFile() {
		// expectedException.expect(XmlException.class);
		File testXml = new File(
				"C:\\Users\\Aleksei_Ivshin@epam.com\\Documents\\TEST\\xmls\\xmlExampleCorrupted.xml");
		try {
			provider.parse(testXml);
			fail("File was corrupted, but was readed!");
		} catch (FileNotFoundException e) {
			fail("Must be other error!");
		} catch (XmlException e) {
		}

	}
	
	@Category(SpeedTest.class)
	@Test
	public void testReadSpeedOneBig(){
		File testXml = new File("C:\\Users\\Aleksei_Ivshin@epam.com\\Documents\\TEST\\xmlSpeedReadTest\\correct.xml");
		try {
			provider.parse(testXml);
		} catch (FileNotFoundException | XmlException e) {
			fail(e.getMessage());
			return;
		}
		for (int i = 0; i < 1; i++) {
			PaymentXml payment = provider.getNextPayment();
			assertNotNull("Unexpected payment equals null", payment);
		}
		int count = 0;
		while(provider.getNextPayment()!=null){
			count++;
		}
		assertNotEquals(count, 0);
		System.err.println("[JAXB]Payment count = "+count);
	}

}
