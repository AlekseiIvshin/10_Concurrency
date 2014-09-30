package appservice;

import xml.FactoryException;
import mapper.Mapper;
import common.FileProvider;
import concurrency.quequestorages.Drop;
import concurrency.quequestorages.FileStorage;

public interface ServiceFactory {
	AppService createService() throws FactoryException;
}
