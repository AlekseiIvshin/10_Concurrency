package dao;

import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;

import dao.entities.PaymentEntity;

public interface PaymentDAO {

	void add(PaymentEntity payment) throws IllegalArgumentException, TransactionRequiredException, EntityExistsException;
}
