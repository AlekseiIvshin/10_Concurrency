package concurrency;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import concurrency.queuestorages.files.FileSetter;

/**
 * Object watch to directories, get created files from these directories and set
 * file to queue.
 * 
 * @author Aleksei_Ivshin
 *
 */
public class FileWatcher implements Runnable {

	final static Logger logger = LoggerFactory.getLogger(FileWatcher.class);

	private final WatchService watcher;
	/**
	 * File queue.
	 */
	private final FileSetter fileStorage;
	/**
	 * List of watched directories.
	 */
	private Set<File> watchigDirectories;

	/**
	 * File filter: get files which name end with '.xml' and no start with 'tmp'
	 */
	private static final FileFilter filter = new FileFilter() {

		@Override
		public boolean accept(File pathname) {
			return pathname.getName().endsWith(".xml")
					&& !pathname.getName().startsWith("tmp");
		}
	};

	/**
	 * Create file watcher for setted file queue.
	 * 
	 * @param storage
	 *            file queue
	 * @throws IOException
	 */
	public FileWatcher(FileSetter storage) throws IOException {
		this.fileStorage = storage;
		watcher = FileSystems.getDefault().newWatchService();
		watchigDirectories = new HashSet<File>();
	}

	/**
	 * Add new directory for watching it.
	 * 
	 * @param directory
	 *            new existing directory
	 * @throws IOException
	 */
	public void addDirectory(File directory) throws IOException {
		// Check on exist and argument is directory
		if (!directory.exists() || !directory.isDirectory()) {
			throw new IOException(directory
					+ " is not readable or not directory");
		}
		// if directory already exist - do nothing
		if (watchigDirectories.contains(directory)) {
			logger.debug(
					"[File watcher] already [contains] '{}' in [directories]",
					directory.getAbsolutePath());
			return;
		}
		// register directory to watcher
		Path dir = directory.toPath();
		try {
			dir.register(watcher, ENTRY_CREATE);
			watchigDirectories.add(directory);
			logger.info("[File watcher] add '{}' to [directories]",
					directory.getAbsolutePath());
		} catch (IOException e) {
			logger.error("Watch key not registred", e);
			throw e;
		}
	}

	@Override
	public void run() {
		if (watchigDirectories.isEmpty()) {
			logger.info("[File wathcer] have not any items in [directories]. [File watcher] service stopped");
			return;
		}
		logger.info("[File watcher] [Starting]");
		setExistingFilesToStorage();
		scanDirectory();
	}

	/**
	 *  Check on ready this file watcher.
	 * @return true - ready, false - not ready.
	 */
	public boolean isReady() {
		return watchigDirectories.size() > 0;
	}

	/**
	 *  Get watched directories list.
	 * @return set of directories.
	 */
	public Set<File> getDirectories() {
		return watchigDirectories;
	}

	/**
	 * Scan directory and set just created files to file queue.
	 */
	private void scanDirectory() {
		logger.info("[Start][Watch] to [directories]");
		while (!Thread.interrupted()) {
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException e) {
				logger.error("Get watcher key", e);
				return;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				if (kind == OVERFLOW) {
					continue;
				}

				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				File fileName = ev.context().toFile();
				setFileToStorage(fileName);
			}
			key.reset();
		}
	}

	/**
	 * Set already existing files to queue.
	 */
	private void setExistingFilesToStorage() {
		for (File watchedDir : watchigDirectories) {
			logger.info("[Set][Files] from '{}' to [file queque]",
					watchedDir.getAbsolutePath());
			File[] files = watchedDir.listFiles(filter);
			for (File f : files) {
				if (!setFileToStorage(f)) {
					return;
				}
			}
		}
	}

	/**
	 * Set file to file queue.
	 * @param file new file for queue
	 * @return true - all right, false - file queue was closed(interrupted)
	 */
	private boolean setFileToStorage(File file) {
		if (fileStorage.setFile(file)) {
			logger.debug("[Set][File] '{}' to [file queque]: SUCCESS",
					file.getName());
			return true;
		} else {
			logger.error("[Set][File] '" + file.getName()
					+ "' to [file queque]: ERROR");
			return false;
		}
	}
}
