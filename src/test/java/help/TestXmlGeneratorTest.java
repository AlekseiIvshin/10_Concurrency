package help;

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
						"src\\test\\resources\\xxl",
						100, 100);
	}

}
