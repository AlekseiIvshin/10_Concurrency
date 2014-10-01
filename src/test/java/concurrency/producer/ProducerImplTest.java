package concurrency.producer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mapper.Mapper;
import mapper.MapperImpl;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import xml.elements.PaymentXml;
import xml.provider.JAXBProviderFactory;
import xml.provider.XmlProvider;
import common.FactoryException;
import common.FileProvider;
import common.FileProviderImpl;
import common.SlowTest;
import common.XmlException;
import concurrency.quequestorages.drop.Drop;
import concurrency.quequestorages.drop.DropGetter;
import concurrency.quequestorages.drop.DropImpl;
import concurrency.quequestorages.drop.DropSetter;
import concurrency.quequestorages.files.FileStorage;
import concurrency.quequestorages.files.FileStorageImpl;
import domain.PaymentDomain;
import domain.PaymentDomainImpl;
import static org.mockito.Mockito.*;

public class ProducerImplTest {
	ExecutorService executorService;
	ProducerImpl producerWithMocks;
	Producer producer;
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
	PaymentDomainImpl paymentDomainMock = mock(PaymentDomainImpl.class);
	@Mock
	File xmlFileMock = mock(File.class);

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() {
		executorService = Executors.newCachedThreadPool();
		producerWithMocks = new ProducerImpl(dropMock, mapperMock,
				fileProviderMock, xmlProviderMock, fileStorageMock);
	}

	@Test
	public void testNullConstructorArguments() {
		exception.expect(NullPointerException.class);
		ProducerImpl producer = new ProducerImpl(null, mapperMock, 
				fileProviderMock, xmlProviderMock, fileStorageMock);
	}

	@Test
	public void testTransferNotFoundFile() throws XmlException, IOException {
		exception.expect(FileNotFoundException.class);
		doThrow(FileNotFoundException.class).when(xmlProviderMock).parse(
				xmlFileMock);
		when(xmlProviderMock.getNextPayment()).thenReturn(null);
		when(fileStorageMock.getNextFile()).thenReturn(xmlFileMock);
		when(fileProviderMock.prepareFile(xmlFileMock)).thenReturn(
				xmlFileMock);
		producerWithMocks.transfer();
		verify(xmlProviderMock, atLeastOnce()).getNextPayment();
		verify(fileStorageMock, atLeastOnce()).getNextFile();
		verify(fileProviderMock, atLeastOnce()).prepareFile(xmlFileMock);
	}

	@Test
	public void testTransferXmlException() throws XmlException, IOException {
		exception.expect(XmlException.class);
		doThrow(XmlException.class).when(xmlProviderMock).parse(xmlFileMock);
		when(xmlProviderMock.getNextPayment()).thenReturn(null);
		when(fileStorageMock.getNextFile()).thenReturn(xmlFileMock);
		when(fileProviderMock.prepareFile(xmlFileMock)).thenReturn(
				xmlFileMock);
		producerWithMocks.transfer();
		verify(xmlProviderMock, atLeastOnce()).getNextPayment();
		verify(fileStorageMock, atLeastOnce()).getNextFile();
		verify(fileProviderMock, atLeastOnce()).prepareFile(xmlFileMock);
	}

	@Ignore
	@Category(SlowTest.class)
	@Test
	public void testTransfer() {
		FileProvider fileProvider = new FileProviderImpl(new File(
				"src\\test\\resources\\temp"));
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
		ProducerImpl prod = new ProducerImpl(drop, mapper, fileProvider,
				xmlProvider, fileStorage);
		executorService.execute(prod);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
