package help;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import org.junit.Test;

public class TestXmlGeneratorTest {

	@Test
	public void testGenerate() {
		TestXmlGenerator generator;
		try {
			generator = new TestXmlGenerator();
		} catch (JAXBException e) {
			e.printStackTrace();
			return;
		}
		generator
				.generate(
						"C:\\Users\\Aleksei_Ivshin@epam.com\\Documents\\TEST\\xxx",
						100, 100);
	}

}
