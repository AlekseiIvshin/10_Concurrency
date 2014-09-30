package concurrency;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mapper.MapperImpl;

import org.junit.Before;
import org.junit.Test;

import concurrency.quequestorages.FileStorage;
import concurrency.quequestorages.FileStorageImpl;
import app.ServiceException;

public class FileWatcherTest {

	private ExecutorService executorService;
	File directory;
	FileStorage fileStorage;
	WatchService watcher;
	
	@Before
	public void setUp(){
		directory = new File("src\\test\\resources");
		fileStorage = new FileStorageImpl(10);
		if(!directory.canRead() || !directory.isDirectory()){
			return;
		}
		executorService = Executors.newCachedThreadPool();
		try {
			watcher = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			fail("Can't create watcher");
		}
		
		
		if(!directory.isDirectory()){
			fail(directory+" is not readable or not directory");
		}
		Path dir = directory.toPath();
		try {
			WatchKey key = dir.register(watcher, ENTRY_CREATE);
		} catch (IOException e) {
			fail("Watch key registry");
		}
	}
	
	@Test
	public void test() {
		FileWatcher fileWatcher;
		try {
			fileWatcher = new FileWatcher(directory, fileStorage);
		} catch (IOException e1) {
			fail("File watcher initialize");
			return;
		}
		executorService.execute(fileWatcher);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
