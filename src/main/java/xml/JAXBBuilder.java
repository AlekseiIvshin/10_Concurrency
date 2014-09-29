package xml;

import java.io.File;
import java.io.IOException;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import common.ConfigReader;

public class JAXBBuilder implements ParserBuilder {

	@Override
	public XmlParser build() throws XmlException {
		try {
			return new JAXBParser(initSchema());
		} catch (XmlException e) {
			throw new XmlException("Can't create parser:"+e.getMessage());
		}
	}


	private Schema initSchema() throws XmlException{
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		String schemaLoc;
		try {
			schemaLoc = new ConfigReader().getValue("schemaLocation");
		} catch (IOException e) {
			throw new XmlException(e.getMessage());
		}
		File schemaFile = new File(schemaLoc);
		try {
			return factory.newSchema(schemaFile);
		} catch (SAXException e1) {
			throw new XmlException(e1.getMessage());
		}
	}
}
