package xml.provider;

import xml.FactoryException;

public interface XmlProviderFactory {

	XmlProvider createProvider() throws FactoryException;
}
