package dao.payment;

public interface MembersDAO {

	PaymentMember findByAccount(String account);
	
}
