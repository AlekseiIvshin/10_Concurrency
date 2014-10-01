package concurrency.quequestorages.files;

import java.io.File;

public interface FileStorage extends FileStorageReadOnly{

	boolean setFile(File f);
}
