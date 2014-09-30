package concurrency.quequestorages;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileStorageImpl implements FileStorage {

	final static Logger logger = LoggerFactory.getLogger(FileStorageImpl.class);
	private final BlockingQueue<File> queue;

	public FileStorageImpl(int queueSize) {
		queue = new ArrayBlockingQueue<File>(queueSize);
	}

	@Override
	public synchronized File getNextFile() {
		return queue.poll();
	}

	@Override
	public synchronized boolean setFile(File f) {
		logger.info("Added {}. In storage {} items.", f.getName(), queue.size());
		return queue.offer(f);
	}
}
