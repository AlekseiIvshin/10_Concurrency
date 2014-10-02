package appservice;

import mapper.Mapper;
import common.FactoryException;
import common.FileProvider;
import concurrency.quequestorages.drop.Drop;
import concurrency.quequestorages.files.FileStorage;

public interface ServiceFactory {
	AppService createService() throws FactoryException;
	String getInitInfo();
}
