package xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import concurrency.Producer;

public class FileUtils {

	final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

	private final static FilenameFilter filter = new FilenameFilter() {

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".xml") && !name.startsWith("tmp");
		}
	};

	public static File getNextTmpFile(File fromDirectory, File destDirectory,
			boolean deleteRealFile) {

		File dest = null;
		File[] files = fromDirectory.listFiles(filter);
		if (files.length == 0) {
			return null;
		}

		File f = files[0];
		// TODO: nonoTime() - it's efficient???
		try {
			dest = File.createTempFile(
					"tmp" + f.hashCode() + System.nanoTime(), ".xml",
					destDirectory);
			copyFileUsingStream(files[0], dest);
			logger.info(files[0].getName() + " -> " + dest.getName());
			files[0].delete();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return dest;
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
}
