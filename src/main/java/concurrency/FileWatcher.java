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

	
	public void addDirectory(File directory) throws IOException {
		if (!directory.isDirectory()) {
			throw new IOException(directory
					+ " is not readable or not directory");
		}
		if (watchigDirectories.contains(directory)) {
			logger.debug("[File watcher] already [contains] '{}' in [directories]",
					directory.getAbsolutePath());
			return;
		}
		Path dir = directory.toPath();
		try {
			dir.register(watcher, ENTRY_CREATE);
			watchigDirectories.add(directory);
			logger.info("[File watcher] add '{}' to [directories]", directory.getAbsolutePath());
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

	public boolean isReady() {
		return watchigDirectories.size() > 0;
	}

	public Set<File> getDirectories() {
		return watchigDirectories;
	}

	private void scanDirectory() {
		logger.info("[Start][Watch] to [directories]");
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
				if(fileStorage.setFile(fileName)){
					logger.debug("[Set][File] '{}' to [file queque]: SUCCESS",fileName.getName());
				} else {
					logger.error("[Set][File] '"+fileName.getName()+"' to [file queque]: ERROR");
					return;
				}
			}
			key.reset();
		}
	}

	private void setExistingFilesToStorage() {
		for (File watchedDir : watchigDirectories) {
			logger.info("[Set][Files] from '{}' to [file queque]",
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
