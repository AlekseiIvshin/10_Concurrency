package appservice;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import mapper.MapperImpl;

import org.junit.Before;
import org.junit.Test;

import xml.provider.StreamProviderFactory;
import common.exception.ServiceException;
import common.fileprovider.FileProviderFactoryImpl;
import concurrency.queuestorages.drop.DropImpl;
import concurrency.queuestorages.files.FileStorageImpl;

public class AppServiceImplTest {

	AppServiceImpl appservice;

	@Before
	public void setUp() {
		try {
			appservice = new AppServiceImpl(new DropImpl(10), new MapperImpl(),
					new FileStorageImpl(10), new FileProviderFactoryImpl(
							new File("src\\test\\resources\\temp")),new StreamProviderFactory(), 2, 2,
					new File("src\\test\\resources"));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testAddWatchingDirectory() {
		try {
			appservice.addWatchingDirectory("src\\test\\resources\\xxl");
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testStartService() {
		try {
			appservice.addWatchingDirectory("src\\test\\resources\\xxl");
			appservice.startService();
		} catch (ServiceException | IOException e) {
			fail(e.getMessage());
			return;
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
		appservice.stopService();

	}

	@Test
	public void testStopService() {
		appservice.stopService();
	}

}
