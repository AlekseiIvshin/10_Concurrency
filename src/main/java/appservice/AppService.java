package appservice;

import java.io.IOException;

import common.exception.ServiceException;

/**
 * Main mediator. 
 * @author Aleksei_Ivshin
 *
 */
public interface AppService {
	
	/**
	 * Add directory for watching and getting files from it.
	 * @param directory existing directory
	 * @throws IOException
	 */
	public void addWatchingDirectory(String directory) throws IOException;

	/**
	 * Start work service.
	 * @throws ServiceException
	 */
	public void startService() throws ServiceException;
	
	/**
	 * Stop work service.
	 */
	public void stopService();
	
}
