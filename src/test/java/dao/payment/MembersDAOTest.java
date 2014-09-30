package dao.payment;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dao.MembersDAO;
import dao.MembersDAOImpl;
import dao.entities.PaymentMember;

public class MembersDAOTest {
	private MembersDAO dao;
	@Before
	public void setUp(){
		dao = new MembersDAOImpl();
	}
	@Test
	public void testFindByAccount() {
		PaymentMember memberImpl = dao.findByAccount("10000000000000000000");
		assertNotNull(memberImpl);
	}

}
