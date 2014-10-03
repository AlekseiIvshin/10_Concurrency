package concurrency.producer;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mapper.Mapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import xml.elements.PaymentXml;
import xml.provider.XmlProvider;

import common.FileProvider;
import common.XmlException;

import concurrency.queuestorages.drop.Drop;
import concurrency.queuestorages.drop.DropSetter;
import concurrency.queuestorages.files.FileStorage;
import domain.PaymentDomainImpl;

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

}
