package concurrency;

import static org.junit.Assert.*;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import mapper.MapperImpl;

import org.junit.Before;
import org.junit.Test;

import common.FileProviderImpl;

import concurrency.Drop;
import concurrency.DropImpl;
import concurrency.Producer;

public class ProducerTest {

	Producer prod;
	Drop drop;
	private ExecutorService executorService;
	File directory;
	
	@Before
	public void setUp(){
		drop = new DropImpl(10);
		directory = new File("src\\test\\resources");
		if(!directory.canRead() || !directory.isDirectory()){
			return;
		}
		try {
			prod = new Producer(drop, new MapperImpl(), new FileProviderImpl(directory));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executorService = Executors.newCachedThreadPool();
	}
	

	@Test
	public void testSetPayment() {
		executorService.execute(prod);
		executorService.execute(prod);
		executorService.execute(prod);
		try {
			executorService.awaitTermination(2,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
