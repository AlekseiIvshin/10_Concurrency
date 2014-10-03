package concurrency.quequestorages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import concurrency.queuestorages.files.FileStorageImpl;

public class FileStorageImplTest {

	FileStorageImpl files;
	@Mock
	File f = mock(File.class); 
	
	@Before
	public void setUp(){
		files = new FileStorageImpl(10);
	}

	@Test
	public void testGetPayment() {
		File f = files.getNextFile();
		assertNull("File must be null", f);
	}

	@Test
	public void testSetAndGetPayment() {
		files.setFile(f);
		File gettedFile = files.getNextFile();
		assertNotNull("Expected not null payment",gettedFile);
		assertEquals(f, gettedFile);
		assertNull(files.getNextFile());
	}
	
	@Test
	public void testSetterAndGetter(){

		File f1 = new File("src\\test\\resources\\1.xml");
		File f2 = new File("src\\test\\resources\\2.xml");
		File f3 = new File("src\\test\\resources\\3.xml");
		File f4 = new File("src\\test\\resources\\4.xml");
		
		files.setFile(f1);
		files.setFile(f2);
		files.setFile(f3);
		files.setFile(f4);
		
		assertEquals(f1, files.getNextFile());
		assertEquals(f2, files.getNextFile());
		assertEquals(f3, files.getNextFile());
		assertEquals(f4, files.getNextFile());
	}

}
