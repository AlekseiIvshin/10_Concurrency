package appservice;

import java.io.IOException;
import java.util.Map;

public interface AppService {
	
	public void addWatchingDirectory(String directory) throws IOException;

	public void startService() throws ServiceException;
	
	public Map<String, String> getConfigurations();
	
	
	public void stopService();
}
