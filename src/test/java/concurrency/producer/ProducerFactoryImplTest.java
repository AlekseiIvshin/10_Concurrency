package concurrency.producer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import mapper.Mapper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import xml.provider.XmlProvider;
import common.FactoryException;
import common.FileProvider;
import concurrency.quequestorages.drop.Drop;
import concurrency.quequestorages.files.FileStorage;

@RunWith(JUnit4.class)
public class ProducerFactoryImplTest {

	ProducerFactoryImpl factory;
	@Mock
	Drop drop = mock(Drop.class);
	@Mock
	Mapper mapper = mock(Mapper.class);
	@Mock
	FileStorage fileStorage = mock(FileStorage.class);
	@Mock
	XmlProvider xmlProvider = mock(XmlProvider.class);
	@Mock
	FileProvider fileProv = mock(FileProvider.class);
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp(){
		factory = new ProducerFactoryImpl();
	}
	
	@Test
	public void testAddDropStorage() {
		assertNotNull(factory.setDropStorage(drop));
	}

	@Test
	public void testAddMapper() {
		assertNotNull(factory.setMapper(mapper));
	}

	@Test
	public void testAddFileQuequeStorage() {
		assertNotNull(factory.setFileQuequeStorage(fileStorage));
	}

	@Test
	public void testAddXmlProvider() {
		assertNotNull(factory.setXmlProvider(xmlProvider));
	}

	@Test
	public void testAddFileProvider() {
		assertNotNull(factory.setFileProvider(fileProv));
	}

	@Test
	public void testCreateProducerWithoutComponents() throws FactoryException {
		exception.expect(FactoryException.class);
		exception.expectMessage("components are null or weren't setted");
		assertNull(factory.createProducer());
			
	}
	

	@Test
	public void testCreateProducer() throws FactoryException {
		assertNotNull(factory.setDropStorage(drop));
		assertNotNull(factory.setMapper(mapper));
		assertNotNull(factory.setFileQuequeStorage(fileStorage));
		assertNotNull(factory.setXmlProvider(xmlProvider));
		assertNotNull(factory.setFileProvider(fileProv));
		assertNotNull(factory.createProducer());	
	}
	@Test
	public void testCreateProducerError() throws FactoryException {
		exception.expect(FactoryException.class);
		exception.expectMessage("components are null or weren't setted");
		assertNotNull(factory.setDropStorage(drop));
		assertNotNull(factory.setMapper(mapper));
		assertNotNull(factory.setFileQuequeStorage(fileStorage));
		//assertNotNull(factory.setXmlProvider(xmlProvider));
		assertNotNull(factory.setFileProvider(fileProv));
		assertNull(factory.createProducer());	
	}

}
