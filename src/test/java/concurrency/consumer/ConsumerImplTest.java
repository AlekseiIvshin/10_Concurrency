package concurrency.consumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityExistsException;

import mapper.Mapper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import concurrency.queuestorages.drop.Drop;
import concurrency.queuestorages.drop.DropGetter;
import dao.PaymentDAO;
import dao.entities.PaymentEntity;
import domain.PaymentDomain;

public class ConsumerImplTest {

	ConsumerImpl consumer;
	@Mock
	DropGetter dropMock = mock(Drop.class);
	@Mock
	Mapper mapperMock = mock(Mapper.class);
	@Mock
	PaymentDAO daoMock = mock(PaymentDAO.class);
	@Mock
	PaymentDomain paymentDomainMock = mock(PaymentDomain.class);
	@Mock
	PaymentEntity paymentEntityMock = mock(PaymentEntity.class);
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testInit() {
		assertNotNull(new ConsumerImpl(dropMock, mapperMock, daoMock));
	}
	
	@Test
	public void testInitWithNullArgumentConstructor() {
		ConsumerImpl consumer=null;
		try{
			consumer = new ConsumerImpl(null, mapperMock, daoMock);
			fail("Drop is null, But it initialize");
		}catch (NullPointerException e){ }
		try{
			consumer = new ConsumerImpl(dropMock, null, daoMock);
			fail("Mapper is null, But it initialize");
		}catch (NullPointerException e){ }
		try{
			consumer = new ConsumerImpl(dropMock, mapperMock, null);
			fail("PayentDAO is null, But it initialize");
		}catch (NullPointerException e){ }
		assertNull(consumer);
	}

	@Test
	public void testTransferIdealWork(){
		when(dropMock.getPayment()).thenReturn(paymentDomainMock);
		when(mapperMock.map(paymentDomainMock, PaymentEntity.class)).thenReturn(paymentEntityMock);
		doNothing().when(daoMock).add(paymentEntityMock);
		ConsumerImpl consumer = new ConsumerImpl(dropMock, mapperMock, daoMock);
		consumer.transfer();
		verify(daoMock,atLeastOnce()).add(paymentEntityMock);
		verify(dropMock,atLeastOnce()).getPayment();
		verify(mapperMock,atLeastOnce()).map(paymentDomainMock, PaymentEntity.class);
	}
	
	@Test
	public void testTransfer(){
		when(dropMock.getPayment()).thenReturn(null).thenReturn(paymentDomainMock);
		when(mapperMock.map(paymentDomainMock, PaymentEntity.class)).thenReturn(paymentEntityMock);
		doNothing().when(daoMock).add(paymentEntityMock);
		ConsumerImpl consumer = new ConsumerImpl(dropMock, mapperMock, daoMock);
		consumer.transfer();
		verify(daoMock,atLeastOnce()).add(paymentEntityMock);
		verify(dropMock,times(2)).getPayment();
		verify(mapperMock,atLeastOnce()).map(paymentDomainMock, PaymentEntity.class);
	}
	
	@Test
	public void testTransferWithDAOError(){
		exception.expect(IllegalArgumentException.class);
		when(dropMock.getPayment()).thenReturn(null).thenReturn(paymentDomainMock);
		when(mapperMock.map(paymentDomainMock, PaymentEntity.class)).thenReturn(paymentEntityMock);
		doThrow(IllegalArgumentException.class).when(daoMock).add(paymentEntityMock);
		ConsumerImpl consumer = new ConsumerImpl(dropMock, mapperMock, daoMock);
		consumer.transfer();
		verify(daoMock,atLeastOnce()).add(paymentEntityMock);
		verify(dropMock,times(2)).getPayment();
		verify(mapperMock,atLeastOnce()).map(paymentDomainMock, PaymentEntity.class);
	}
	

	@Test
	public void testTransferWithDAOExistEntity(){
		when(dropMock.getPayment()).thenReturn(null).thenReturn(paymentDomainMock);
		when(mapperMock.map(paymentDomainMock, PaymentEntity.class)).thenReturn(paymentEntityMock);
		doThrow(EntityExistsException.class).when(daoMock).add(paymentEntityMock);
		ConsumerImpl consumer = new ConsumerImpl(dropMock, mapperMock, daoMock);
		consumer.transfer();
		verify(daoMock,atLeastOnce()).add(paymentEntityMock);
		verify(dropMock,times(2)).getPayment();
		verify(mapperMock,atLeastOnce()).map(paymentDomainMock, PaymentEntity.class);
	}
}
