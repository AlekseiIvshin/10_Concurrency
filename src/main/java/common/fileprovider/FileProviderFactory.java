package common.fileprovider;

import common.exception.FactoryException;

public interface FileProviderFactory {

	FileProvider createProvider() throws FactoryException;
}
