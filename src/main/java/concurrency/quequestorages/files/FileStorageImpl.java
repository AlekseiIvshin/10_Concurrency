package concurrency.quequestorages.files;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.PaymentDomain;

public class FileStorageImpl implements FileStorage {

	final static Logger logger = LoggerFactory.getLogger(FileStorageImpl.class);
	private final BlockingQueue<File> queue;

	public FileStorageImpl(int queueSize) {
		queue = new ArrayBlockingQueue<File>(queueSize);
	}

	public synchronized File getNextFile() {
		File f = null;
		while ((f = queue.poll()) == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				return null;
			}
		}
		notifyAll();
		return f;
	}

	public synchronized boolean setFile(File f) {
		if (f == null) {
			return true;
		}
		while (!queue.offer(f)) {
			try {
				wait();
			} catch (InterruptedException e) {
				return false;
			}
		}
		notifyAll();
		return true;
	}
}
