package dao.payment;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PaymentDAOImpl implements PaymentDAO {

	EntityManager entityManager;
	
	public PaymentDAOImpl(){
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("10_Concurrency");
		entityManager = emf.createEntityManager();
	}
	
	@Override
	public boolean add(PaymentEntity payment) {
		try{
			entityManager.getTransaction().begin();
			entityManager.persist(payment);
			entityManager.getTransaction().commit();
			return true;
		} catch(EntityExistsException e){
			return false;
		}
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<PaymentEntity> get(int offser, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

}
