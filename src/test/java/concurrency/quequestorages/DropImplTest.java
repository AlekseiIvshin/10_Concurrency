package concurrency.quequestorages;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import concurrency.quequestorages.drop.DropImpl;
import domain.PaymentDomain;
import static org.mockito.Mockito.*; 


public class DropImplTest {

	DropImpl drop;
	@Mock
	PaymentDomain paymentDomain = mock(PaymentDomain.class); 
	
	@Before
	public void setUp(){
		drop = new DropImpl(10);
	}

	@Test
	public void testGetPayment() {
		PaymentDomain paymentDomain = drop.getPayment();
		assertNull("Payment must be null", paymentDomain);
	}

	@Test
	public void testSetAndGetPayment() {
		drop.setPayment(paymentDomain);
		PaymentDomain gettedPayment = drop.getPayment();
		assertNotNull("Expected not null payment",gettedPayment);
		assertEquals(paymentDomain, gettedPayment);
		assertNull(drop.getPayment());
	}
	

}
