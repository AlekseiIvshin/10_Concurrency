package concurrency.producer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mapper.Mapper;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import xml.elements.PaymentXml;
import xml.provider.XmlProvider;
import common.FactoryException;
import common.FileProvider;
import common.XmlException;
import concurrency.quequestorages.Drop;
import concurrency.quequestorages.FileStorage;
import domain.PaymentDomain;
import static org.mockito.Mockito.*;

public class ProducerImplTest {
	ExecutorService executorService;
	ProducerImpl producer;
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
	FileProvider fileProvider = mock(FileProvider.class);
	@Mock
	PaymentXml paymentXml = mock(PaymentXml.class);
	@Mock
	PaymentDomain paymentDomain = mock(PaymentDomain.class);
	@Mock
	File xmlFile = mock(File.class);
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp(){
		executorService = Executors.newCachedThreadPool();
		factory = new ProducerFactoryImpl();
		assertNotNull(factory.setDropStorage(drop));
		assertNotNull(factory.setMapper(mapper));
		assertNotNull(factory.setFileQuequeStorage(fileStorage));
		assertNotNull(factory.setXmlProvider(xmlProvider));
		assertNotNull(factory.setFileProvider(fileProvider));
		producer = new ProducerImpl(drop, mapper, fileProvider, xmlProvider, fileStorage);
	}
	@Ignore
	@Test
	public void testRun() {
		executorService.execute(producer);
	}
	
	@Test
	public void testTransferNotFoundFile() throws FileNotFoundException, XmlException{
		exception.expect(FileNotFoundException.class);
		doThrow(FileNotFoundException.class).when(xmlProvider).parse(xmlFile);
		when(xmlProvider.getNextPayment()).thenReturn(null);
		when(fileStorage.getNextFile()).thenReturn(xmlFile);
		when(fileProvider.copyToTempFile(xmlFile,true)).thenReturn(xmlFile);
		producer.transfer();
		verify(xmlProvider, atLeastOnce()).getNextPayment();
		verify(fileStorage, atLeastOnce()).getNextFile();
		verify(fileProvider, atLeastOnce()).copyToTempFile(xmlFile,true);
	}
	

	@Test
	public void testTransferXmlException() throws FileNotFoundException, XmlException{
		exception.expect(XmlException.class);
		doThrow(XmlException.class).when(xmlProvider).parse(xmlFile);
		when(xmlProvider.getNextPayment()).thenReturn(null);
		when(fileStorage.getNextFile()).thenReturn(xmlFile);
		when(fileProvider.copyToTempFile(xmlFile,true)).thenReturn(xmlFile);
		producer.transfer();
		verify(xmlProvider, atLeastOnce()).getNextPayment();
		verify(fileStorage, atLeastOnce()).getNextFile();
		verify(fileProvider, atLeastOnce()).copyToTempFile(xmlFile,true);
	}
	

	@Test
	public void testTransfer() throws FileNotFoundException, XmlException {
		when(xmlProvider.getNextPayment())
				.thenReturn(paymentXml)
				.thenReturn(null);
		when(fileStorage.getNextFile()).thenReturn(xmlFile);
		when(fileProvider.copyToTempFile(xmlFile,true)).thenReturn(xmlFile);
		when(mapper.map(paymentXml, PaymentDomain.class)).thenReturn(paymentDomain);
		when(drop.setPayment(paymentDomain)).thenReturn(true);
		doNothing().when(xmlProvider).parse(xmlFile);
		producer.transfer();
		verify(xmlProvider, atLeastOnce()).getNextPayment();
		verify(fileStorage, atLeastOnce()).getNextFile();
		verify(fileProvider, atLeastOnce()).copyToTempFile(xmlFile,true);
	}

}
