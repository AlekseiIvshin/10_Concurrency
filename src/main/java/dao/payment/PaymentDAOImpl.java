package dao.payment;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

public class PaymentDAOImpl implements PaymentDAO {

	EntityManager entityManager;
	
	public PaymentDAOImpl(EntityManager entityManager){
		this.entityManager = entityManager;
	}
	
	@Override
	public boolean add(PaymentEntity payment) {
		try{
			entityManager.persist(payment);
			entityManager.flush();
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
