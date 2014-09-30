package app;

import static org.junit.Assert.*;

import java.io.File;

import mapper.Mapper;
import mapper.MapperImpl;

import org.junit.Test;

import concurrency.quequestorages.Drop;
import concurrency.quequestorages.DropImpl;
import concurrency.quequestorages.FileStorage;
import concurrency.quequestorages.FileStorageImpl;

public class AppServiceTest {

	private final static String readedDirectory = "src\\test\\resources";
	@Test
	public void test() {
		File f = new File(readedDirectory);
		if(!f.canRead() || !f.isDirectory()){
			fail("It must be directory");
		}
		Drop drop = new DropImpl(10);
		FileStorage fileStorage = new FileStorageImpl(10);
		Mapper mapper = new MapperImpl();
		AppServiceImpl app;
		try {
			app = new AppServiceImpl(drop, mapper,fileStorage);
		} catch (ServiceException e1) {
			fail("Error");
			return;
		}
		app.startService(f,2,2);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			app.stopService();
		}
	}

}
