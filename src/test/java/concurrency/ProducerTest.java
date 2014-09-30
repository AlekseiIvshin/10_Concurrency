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
import concurrency.producer.ProducerImpl;
import concurrency.quequestorages.Drop;
import concurrency.quequestorages.DropImpl;

public class ProducerTest {

	ProducerImpl prod;
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
			prod = new ProducerImpl(drop, new MapperImpl(), new FileProviderImpl(directory));
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
