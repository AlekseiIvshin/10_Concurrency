package xml.provider;

import javax.xml.bind.JAXBException;

import common.exception.FactoryException;

public class StreamProviderFactory implements XmlProviderFactory {

	@Override
	public XmlProvider createProvider() throws FactoryException {
		try {
			return new JAXBplusStAXProvider();
		} catch (JAXBException e) {
			throw new FactoryException(e.getMessage());
		}
	}

}
