package dao.payment;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
