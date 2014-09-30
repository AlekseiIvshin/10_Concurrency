package dao;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import dao.entities.PaymentEntity;
import dao.entities.PaymentMember;

public class PaymentDAOImpl implements PaymentDAO {

	EntityManagerFactory emf;
	
	public PaymentDAOImpl(){
		emf = Persistence
				.createEntityManagerFactory("10_Concurrency");
	}
	
	@Override
	public boolean add(PaymentEntity payment) {
		EntityManager entityManager = emf.createEntityManager();
		try{
			entityManager.getTransaction().begin();
			MembersDAO members = new MembersDAOImpl();
			PaymentMember payer = members.findByAccount(payment.getPayer().getAccount());
			PaymentMember payee = members.findByAccount(payment.getPayee().getAccount());
			if(payer==null || payee==null){
				entityManager.getTransaction().rollback();
				return false;
			}
			payment.setPayee(payee);
			payment.setPayer(payer);
			entityManager.persist(payment);
			entityManager.getTransaction().commit();
			return true;
		} catch(EntityExistsException e){
			e.printStackTrace();
			return false;
		} finally {
			entityManager.close();
		}
	}

}
