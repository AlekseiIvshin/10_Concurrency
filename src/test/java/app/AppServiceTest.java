package app;

import static org.junit.Assert.*;

import java.io.File;

import mapper.Mapper;
import mapper.MapperImpl;

import org.junit.Test;

import concurrency.Drop;
import concurrency.DropImpl;

public class AppServiceTest {

	private final static String readedDirectory = "src\\test\\resources";
	@Test
	public void test() {
		File f = new File(readedDirectory);
		if(!f.canRead() || !f.isDirectory()){
			fail("It must be directory");
		}
		Drop drop = new DropImpl(10);
		Mapper mapper = new MapperImpl();
		AppService app = new AppService(drop, mapper);
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
