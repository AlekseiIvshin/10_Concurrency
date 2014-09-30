package common;

import java.io.File;

public interface FileProvider {

	File copyToTempFile(File sourceFile,boolean deleteSoruceFile);
	File getTempDestination();
	void close();
	void close(File f);
}
