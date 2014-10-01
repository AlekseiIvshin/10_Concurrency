package appservice;

import mapper.Mapper;
import common.FactoryException;
import common.FileProvider;
import concurrency.quequestorages.Drop;
import concurrency.quequestorages.FileStorage;

public interface ServiceFactory {
	AppService createService() throws FactoryException;
}
