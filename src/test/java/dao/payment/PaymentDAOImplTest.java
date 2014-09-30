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
		
		payment = new PaymentEntity();
		BankEnity bank = new BankEnity();
		bank.setBIC("123123");
		bank.setName("Some name");
		PaymentMember payer = new PaymentMember();
		payer.setAccount("10000000000000000000");
		payer.setAddress("adadsa");
		Document payerDoc = new Document();
		payerDoc.setType("passport");
		payerDoc.setNumber("123123");
		payerDoc.setSeries("0990");
		payer.setDocument(payerDoc);

		PaymentMember payee = new PaymentMember();
		payee.setAccount("10000000000000000001");
		payee.setAddress("adadsa");
		Document payeeDoc = new Document();
		payeeDoc.setType("passport");
		payeeDoc.setNumber("995595");
		payeeDoc.setSeries("0990");
		payee.setDocument(payeeDoc);
		
		payment.setBank(bank);
		payment.setPayee(payee);
		payment.setPayer(payer);
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
