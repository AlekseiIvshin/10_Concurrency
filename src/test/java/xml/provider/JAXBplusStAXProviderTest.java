package xml.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import xml.elements.PaymentXml;
import common.SpeedTest;
import common.exception.FactoryException;
import common.exception.XmlException;

public class JAXBplusStAXProviderTest {
	XmlProvider provider;
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() {
		try {
			provider = new StreamProviderFactory().createProvider();
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
		File testXml = new File("C:\\Users\\Aleksei_Ivshin@epam.com\\Documents\\TEST\\xmls\\correct.xml");
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
				"C:\\Users\\Aleksei_Ivshin@epam.com\\Documents\\TEST\\xmls\\xmlExampleNeverWas.xml");

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
		File testXml = new File("C:\\Users\\Aleksei_Ivshin@epam.com\\Documents\\TEST\\correct.xml");
		try {
			provider.parse(testXml);
		} catch (FileNotFoundException | XmlException e) {
			fail(e.getMessage());
			return;
		}
		
		int count = 0;
		while(provider.getNextPayment()!=null){
			count++;
		}
		assertEquals(count, 2);
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
		
		int count = 0;
		while(provider.getNextPayment()!=null){
			count++;
		}
		assertNotEquals(count, 0);
		System.err.println("[JAXB+StAX]Payment count = "+count);
	}

}
