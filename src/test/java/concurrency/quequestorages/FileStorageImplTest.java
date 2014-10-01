package concurrency.quequestorages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

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

}
