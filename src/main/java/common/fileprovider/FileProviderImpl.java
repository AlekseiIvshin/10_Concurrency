package common.fileprovider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileProviderImpl implements FileProvider {

	final static Logger logger = LoggerFactory
			.getLogger(FileProviderImpl.class);
	private final File destDirectory;

	public FileProviderImpl(File destDirectory) {
		this.destDirectory = destDirectory;
	}

	@Override
	public File prepareFile(File sourceFile) throws IOException {
		return copyToTempFile(sourceFile, true);
	}

	public File copyToTempFile(File sourceFile, boolean deleteSoruceFile)
			throws IOException {
		if (!destDirectory.exists()) {
			throw new IOException("Destination direcotry not exist: "
					+ destDirectory.getAbsolutePath());
		}
		if (!sourceFile.exists()) {
			throw new IOException("Source file not exist: "
					+ sourceFile.getAbsolutePath());
		}
		File dest = null;
		// TODO: nonoTime() - it's efficient???
		try {
			dest = File.createTempFile("tmp" + sourceFile.getName().hashCode()
					+ System.nanoTime(), ".xml", destDirectory);
			copyFileUsingStream(sourceFile, dest);
			logger.debug(sourceFile.getName() + " -> " + dest.getName());
			if (deleteSoruceFile) {
				sourceFile.delete();
			}
		} catch (IOException e1) {
			logger.error("[Copy] to temp file. [Destination directory] '"
					+ destDirectory + "'", e1);
			throw e1;
		}

		return dest;
	}

	@Override
	public void close() {
		if (destDirectory != null && destDirectory.listFiles().length == 0)
			destDirectory.delete();
	}

	private static void copyFileUsingStream(File source, File dest)
			throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
		}
	}

	@Override
	public void close(File f) {
		if (f != null) {
			logger.debug("[Close][File] '{}'", f.getAbsolutePath());
			f.delete();
		}
	}

}
