package dao.payment;

import java.util.List;

public interface PaymentDAO {

	boolean add(PaymentEntity payment);

	void remove();

	List<PaymentEntity> get(int offser, int limit);
}
