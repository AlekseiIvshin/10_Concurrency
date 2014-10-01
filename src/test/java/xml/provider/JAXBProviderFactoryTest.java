package xml.provider;

import static org.junit.Assert.*;

import org.junit.Test;

import common.FactoryException;

public class JAXBProviderFactoryTest {

	@Test
	public void testCreateProvider() {
		try {
			XmlProvider provider = new JAXBProviderFactory().createProvider();
			assertNotNull("Created XML provider is null", provider);
		} catch (FactoryException e) {
			fail(e.getMessage());
		}
	}

}
