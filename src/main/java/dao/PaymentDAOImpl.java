package dao;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TransactionRequiredException;

import dao.entities.PaymentEntity;
import dao.entities.PaymentMember;

public class PaymentDAOImpl implements PaymentDAO {

	EntityManagerFactory emf;

	public PaymentDAOImpl() {
		emf = Persistence.createEntityManagerFactory("10_Concurrency");
	}

	@Override
	public void add(PaymentEntity payment) throws IllegalArgumentException, TransactionRequiredException, EntityExistsException {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		try {
			MembersDAO members = new MembersDAOImpl();
			PaymentMember payer = members.findByAccount(payment.getPayer()
					.getAccount());
			PaymentMember payee = members.findByAccount(payment.getPayee()
					.getAccount());
			// if(payer==null || payee==null){
			// entityManager.getTransaction().rollback();
			// return false;
			// }
			payment.setPayee(payee);
			payment.setPayer(payer);
			entityManager.persist(payment);
			entityManager.getTransaction().commit();
		} catch(EntityExistsException| IllegalArgumentException	| TransactionRequiredException e) {
			entityManager.getTransaction().rollback();
			throw e;
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}
}
