package xml.provider;

import common.exception.FactoryException;

public interface XmlProviderFactory {

	XmlProvider createProvider() throws FactoryException;
}
