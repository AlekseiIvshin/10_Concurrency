package dao.payment;

import java.util.List;

import dao.entities.PaymentEntity;

public interface PaymentDAO {

	boolean add(PaymentEntity payment);
}
