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
			return;
		}
		generator.generate("src\\test\\resources\\xxx", 100, 500);
	}

}
