package xml.provider;

import common.FactoryException;

public interface XmlProviderFactory {

	XmlProvider createProvider() throws FactoryException;
}
