package appservice;

import common.exception.FactoryException;

public interface ServiceFactory {
	AppService createService() throws FactoryException;
	String getInitInfo();
}
