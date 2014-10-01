package xml.provider;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import common.ConfigReader;
import common.FactoryException;

public class JAXBProviderFactory implements XmlProviderFactory {

	@Override
	public XmlProvider createProvider() throws FactoryException {
		try {
			return new JAXBProvider(initSchema());
		} catch ( JAXBException | IOException | SAXException e) {
			throw new FactoryException("Can't create XML provider:"+e.getMessage());
		}
	}
	private Schema initSchema() throws IOException, SAXException{
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		String schemaLoc;
		schemaLoc = new ConfigReader().getValue("schemaLocation");
		File schemaFile = new File(schemaLoc);
		return factory.newSchema(schemaFile);
	}
}
