package appservice;

import common.FactoryException;

public interface ServiceFactory {
	AppService createService() throws FactoryException;
	String getInitInfo();
}
