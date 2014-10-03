package common.fileprovider;

import java.io.File;
import java.io.IOException;

/**
 * Encapsulates preparing files for further work with them.
 * @author Aleksei_Ivshin
 *
 */
public interface FileProvider {

	/**
	 * Prepare file for further work with them
	 * @param sourceFile source file
	 * @return file 
	 * @throws IOException
	 */
	File prepareFile(File sourceFile) throws IOException;
	
	/**
	 * Close provider.
	 */
	void close();
	
	/**
	 * Close work with file.
	 * @param f
	 */
	void close(File f);
}
