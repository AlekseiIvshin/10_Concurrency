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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import appservice.ServiceException;
import concurrency.quequestorages.files.FileStorage;

public class FileWatcher implements Runnable {

	final static Logger logger = LoggerFactory.getLogger(FileWatcher.class);

	private final WatchService watcher;
	private final FileStorage fileStorage;
	private Set<File> watchigDirectories;
	private static final FileFilter filter = new FileFilter() {

		@Override
		public boolean accept(File pathname) {
			return pathname.getName().endsWith(".xml")
					&& !pathname.getName().startsWith("tmp");
		}
	};

	private Object storageLock = new Object();

	public FileWatcher(FileStorage storage) throws IOException {
		this.fileStorage = storage;
		watcher = FileSystems.getDefault().newWatchService();
		watchigDirectories = new HashSet<File>();
	}

	public void initWatcher() {

	}
	
	public void addDirectory(File directory) throws IOException {
		if (!directory.isDirectory()) {
			throw new IOException(directory
					+ " is not readable or not directory");
		}
		if (watchigDirectories.contains(directory)) {
			logger.debug("File watcher already contains {}",
					directory.getAbsolutePath());
			return;
		}
		Path dir = directory.toPath();
		try {
			dir.register(watcher, ENTRY_CREATE);
			watchigDirectories.add(directory);
			logger.info("{} now is watched", directory.getAbsolutePath());
		} catch (IOException e) {
			logger.error("Watch key not registred", e);
			throw e;
		}
	}

	@Override
	public void run() {
		if (watchigDirectories.isEmpty()) {
			logger.info("File wathcers directory list is empty. File watcher service stopped");
			return;
		}
		logger.info("File watcher start work");
		setExistingFilesToStorage();
		scanDirectory();
	}

	public boolean isReady() {
		return watchigDirectories.size() > 0;
	}

	public Set<File> getDirectories() {
		return watchigDirectories;
	}

	private void scanDirectory() {
		logger.info("Start watch to directories (count: {})",
				watchigDirectories.size());
		while (!Thread.interrupted()) {
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException e) {
				logger.error("Get watcher key", e);
				// TODO: WHAT need do?!
				return;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				if (kind == OVERFLOW) {
					continue;
				}

				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				File fileName = ev.context().toFile();
				setFileToQueque(fileName);
				logger.info("Setted {} to file queque", fileName.getName());
			}
			key.reset();
		}
	}

	private void setFileToQueque(File f) {
		synchronized (storageLock) {
			while (!fileStorage.setFile(f)) {
				try {
					storageLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.error("Thread interrupted", e);
					return;
				}
			}
			storageLock.notifyAll();
		}
	}

	private void setExistingFilesToStorage() {
		for (File watchedDir : watchigDirectories) {
			logger.info("Set already exisiting files from {} to queque",
					watchedDir.getAbsolutePath());
			setExistingFiles(watchedDir);
		}
	}

	private void setExistingFiles(File watchedDirecotry) {
		File[] files = watchedDirecotry.listFiles(filter);
		for (File f : files) {
			synchronized (storageLock) {
				while (!fileStorage.setFile(f)) {
					try {
						storageLock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						logger.error("Thread interrupted", e);
						return;
					}
				}
				storageLock.notifyAll();
			}
		}
	}

}
