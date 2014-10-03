package dao;

import dao.entities.PaymentMember;

public interface MembersDAO {

	PaymentMember findByAccount(String account);
	
}
