package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import dao.entities.PaymentMember;

public class MembersDAOImpl implements MembersDAO {

	EntityManagerFactory emf;

	public MembersDAOImpl() {
		emf = Persistence.createEntityManagerFactory("10_Concurrency");
	}

	@Override
	public PaymentMember findByAccount(String account) {
		EntityManager entityManager = emf.createEntityManager();
		PaymentMember result = null;
		try{
		result = (PaymentMember) entityManager
				.createQuery(
						"SELECT p FROM PaymentMember p WHERE p.account=:account")
				.setParameter("account", account).getSingleResult();
		}catch(Exception e){
		} finally{
			entityManager.close();
		}
		return result;
	}

}
