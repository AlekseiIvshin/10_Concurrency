package dao;

import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;

import dao.entities.PaymentEntity;

public interface PaymentDAO {

	boolean add(PaymentEntity payment) throws IllegalArgumentException, TransactionRequiredException, EntityExistsException;
}
