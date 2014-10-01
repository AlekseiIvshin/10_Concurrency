package common;

import java.io.File;
import java.io.IOException;

public interface FileProvider {

	File prepareFile(File sourceFile) throws IOException;
	void close();
	void close(File f);
}
