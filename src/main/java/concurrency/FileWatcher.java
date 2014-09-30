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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileWatcher implements Runnable {

	final static Logger logger = LoggerFactory.getLogger(FileWatcher.class);

	private final File sourceDirectory;
	private final WatchService watcher;
	private final FileStorage fileStorage;
	private static final FileFilter filter = new FileFilter() {
		
		@Override
		public boolean accept(File pathname) {
			return pathname.getName().endsWith(".xml") && !pathname.getName().startsWith("tmp");
		}
	};

	private Object storageLock = new Object();

	public FileWatcher(File sourcePath, FileStorage storage) throws IOException{
		this.fileStorage = storage;
		sourceDirectory = sourcePath;
		watcher = FileSystems.getDefault().newWatchService();
	}
	
	public void initWatcher(){
		
	}
	
	public void addDirectory(File directory) throws IOException{
		if(!directory.isDirectory()){
			throw new IOException(directory+" is not readable or not directory");
		}
		Path dir = directory.toPath();
		try {
			dir.register(watcher, ENTRY_CREATE);
		} catch (IOException e) {
			logger.error("Watch key not registred",e);
			throw e;
		}
	}

	@Override
	public void run() {
		logger.info("File watcher start work");
		logger.info("Set existing files on {} to file queque",sourceDirectory);
		setExistingFilesToStorage();
		logger.info("Start watch to {}", sourceDirectory);
		scanDirectory();
	}
	
	private void scanDirectory(){
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
	
	private void setFileToQueque(File f){
		synchronized (storageLock) {
			while (!fileStorage.setFile(f)) {
				try {
					storageLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.error("Thread interrupted",e);
					return;
				}
			}
			storageLock.notifyAll();
		}
	}
	private void setExistingFilesToStorage(){
		File[] files = sourceDirectory.listFiles(filter);
		for(File f: files){
			synchronized (storageLock) {
				while (!fileStorage.setFile(f)) {
					try {
						storageLock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						logger.error("Thread interrupted",e);
						return;
					}
				}
				storageLock.notifyAll();
			}
		}
	}

}
