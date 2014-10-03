package concurrency.filewatcher;

import java.io.File;
import java.io.IOException;

public interface FileWatcher extends Runnable{

	void addDirectory(File directory) throws IOException;
}
