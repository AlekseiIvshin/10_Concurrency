package dao.payment;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

public class PaymentDAOImplTest {

	private PaymentEntity payment ;
	
	@Before
	public void setUp(){
		
		payment = new PaymentEntityImpl();
		BankEnity bank = new BankEnity();
		bank.setBIC("123123");
		bank.setName("Some name");
		PayerEntiy payerEntiy = new PayerEntiy();
		payerEntiy.setName("Ivanesyan");
		PayeeEntiy payeeEntiy = new PayeeEntiy();
		payeeEntiy.setName("Sarumyan");
		payment.setBank(bank);
		payment.setPayee(payeeEntiy);
		payment.setPayer(payerEntiy);
		payment.setCash(new BigDecimal(1000));
		payment.setDateOfCreate(new Date());
		payment.setDateOfExecute(new Date());
	}
	@Test
	public void testAdd() {
		PaymentDAO paymentDAO = new PaymentDAOImpl();
		paymentDAO.add(payment);
	}

}
