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
		PaymentMemberImpl payer = new PaymentMemberImpl();
		payer.setId(1);
		payer.setAccount("10000000000000000000");
		payer.setAddress("adadsa");
		Document payerDoc = new Document();
		payerDoc.setType("passport");
		payerDoc.setNumber("123123");
		payerDoc.setSeries("0990");
		payer.setDocument(payerDoc);

		PaymentMemberImpl payee = new PaymentMemberImpl();
		payee.setId(2);
		payer.setAccount("10000000000000000002");
		payer.setAddress("adadsa");
		Document payeeDoc = new Document();
		payerDoc.setType("passport");
		payerDoc.setNumber("995595");
		payerDoc.setSeries("0990");
		payer.setDocument(payeeDoc);
		
		payment.setBank(bank);
		payment.setPayee(payer);
		payment.setPayer(payee);
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
