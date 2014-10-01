package concurrency.producer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mapper.Mapper;
import mapper.MapperImpl;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import xml.elements.PaymentXml;
import xml.provider.JAXBProviderFactory;
import xml.provider.XmlProvider;
import xml.provider.XmlProviderFactory;
import common.FactoryException;
import common.FileProvider;
import common.FileProviderImpl;
import common.XmlException;
import concurrency.quequestorages.drop.Drop;
import concurrency.quequestorages.drop.DropImpl;
import concurrency.quequestorages.drop.DropSetter;
import concurrency.quequestorages.files.FileStorage;
import concurrency.quequestorages.files.FileStorageImpl;
import domain.PaymentDomain;
import static org.mockito.Mockito.*;

public class ProducerImplTest {
	ExecutorService executorService;
	ProducerImpl producerWithMocks;
	Producer producer;
	ProducerFactoryImpl factory;
	@Mock
	DropSetter dropMock = mock(Drop.class);
	@Mock
	Mapper mapperMock = mock(Mapper.class);
	@Mock
	FileStorage fileStorageMock = mock(FileStorage.class);
	@Mock
	XmlProvider xmlProviderMock = mock(XmlProvider.class);
	@Mock
	FileProvider fileProviderMock = mock(FileProvider.class);
	@Mock
	PaymentXml paymentXmlMock = mock(PaymentXml.class);
	@Mock
	PaymentDomain paymentDomainMock = mock(PaymentDomain.class);
	@Mock
	File xmlFileMock = mock(File.class);
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp(){
		executorService = Executors.newCachedThreadPool();
		factory = new ProducerFactoryImpl();
		assertNotNull(factory.setDropStorage(dropMock));
		assertNotNull(factory.setMapper(mapperMock));
		assertNotNull(factory.setFileQuequeStorage(fileStorageMock));
		assertNotNull(factory.setXmlProvider(xmlProviderMock));
		assertNotNull(factory.setFileProvider(fileProviderMock));
		producerWithMocks = new ProducerImpl(dropMock, mapperMock, fileProviderMock, xmlProviderMock, fileStorageMock);
	}
	@Ignore
	@Test
	public void testRun() {
		executorService.execute(producer);
	}
	
	@Test
	public void testTransferNotFoundFile() throws FileNotFoundException, XmlException{
		exception.expect(FileNotFoundException.class);
		doThrow(FileNotFoundException.class).when(xmlProviderMock).parse(xmlFileMock);
		when(xmlProviderMock.getNextPayment()).thenReturn(null);
		when(fileStorageMock.getNextFile()).thenReturn(xmlFileMock);
		when(fileProviderMock.copyToTempFile(xmlFileMock,true)).thenReturn(xmlFileMock);
		producerWithMocks.transfer();
		verify(xmlProviderMock, atLeastOnce()).getNextPayment();
		verify(fileStorageMock, atLeastOnce()).getNextFile();
		verify(fileProviderMock, atLeastOnce()).copyToTempFile(xmlFileMock,true);
	}
	

	@Test
	public void testTransferXmlException() throws FileNotFoundException, XmlException{
		exception.expect(XmlException.class);
		doThrow(XmlException.class).when(xmlProviderMock).parse(xmlFileMock);
		when(xmlProviderMock.getNextPayment()).thenReturn(null);
		when(fileStorageMock.getNextFile()).thenReturn(xmlFileMock);
		when(fileProviderMock.copyToTempFile(xmlFileMock,true)).thenReturn(xmlFileMock);
		producerWithMocks.transfer();
		verify(xmlProviderMock, atLeastOnce()).getNextPayment();
		verify(fileStorageMock, atLeastOnce()).getNextFile();
		verify(fileProviderMock, atLeastOnce()).copyToTempFile(xmlFileMock,true);
	}
	

	@Test
	public void testTransfer(){
		FileProvider fileProvider = new FileProviderImpl(new File("src\\test\\resources\\temp"));
		DropSetter drop = new DropImpl(10);
		Mapper mapper = new MapperImpl();
		XmlProvider xmlProvider;
		try {
			xmlProvider = new JAXBProviderFactory().createProvider();
		} catch (FactoryException e) {
			fail(e.getMessage());
			return;
		}
		FileStorage fileStorage = new FileStorageImpl(10);
		fileStorage.setFile(new File("src\\test\\resources\\1.xml"));
		fileStorage.setFile(new File("src\\test\\resources\\2.xml"));
		fileStorage.setFile(new File("src\\test\\resources\\3.xml"));
		fileStorage.setFile(new File("src\\test\\resources\\4.xml"));
//		fileStorage.setFile(new File("src\\test\\resources\\5.xml"));
//		when(fileStorageMock.getNextFile())
//				.thenReturn(new File("src\\test\\resources\\1.xml"))
//				.thenReturn(new File("src\\test\\resources\\2.xml"))
//				.thenReturn(new File("src\\test\\resources\\3.xml"))
//				.thenReturn(new File("src\\test\\resources\\4.xml"))
//				.thenReturn(new File("src\\test\\resources\\5.xml"));
		ProducerImpl prod = new ProducerImpl(drop,mapper, fileProvider,xmlProvider,fileStorage);
		executorService.execute(prod);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
