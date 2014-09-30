package concurrency;

import java.io.File;

public interface FileStorage extends FileStorageReadOnly{

	boolean setFile(File f);
}
