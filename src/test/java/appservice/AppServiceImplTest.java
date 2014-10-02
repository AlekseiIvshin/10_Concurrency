package appservice;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import mapper.MapperImpl;

import org.junit.Before;
import org.junit.Test;

import common.FileProviderImpl;
import concurrency.quequestorages.drop.DropImpl;
import concurrency.quequestorages.files.FileStorageImpl;

public class AppServiceImplTest {

	AppServiceImpl appservice;

	@Before
	public void setUp() {
		try {
			appservice = new AppServiceImpl(new DropImpl(2), new MapperImpl(),
					new FileStorageImpl(2), new FileProviderImpl(new File(
							"C:\\Users\\dnss\\Documents\\TEST\\temp")), 2, 2, new File(
							"C:\\Users\\dnss\\Documents\\TEST"));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testAddWatchingDirectory() {
		try {
			appservice.addWatchingDirectory("C:\\Users\\dnss\\Documents\\TEST\\xxx");
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testStartService() {
		try {
			appservice.addWatchingDirectory("C:\\Users\\dnss\\Documents\\TESTS\\xxx");
			appservice.startService();
		} catch (ServiceException | IOException e) {
			fail(e.getMessage());
			return;
		}
		try {
			Thread.sleep(60000);
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
