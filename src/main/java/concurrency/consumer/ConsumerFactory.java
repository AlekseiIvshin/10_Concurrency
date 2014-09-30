package concurrency.consumer;

import concurrency.quequestorages.Drop;
import dao.PaymentDAO;
import mapper.Mapper;

public interface ConsumerFactory {

	ConsumerFactory addDropStorage(Drop drop);
	ConsumerFactory addMapper(Mapper mapper);
	ConsumerFactory addPaymentDAO(PaymentDAO dao);
	Consumer createConsumer();
	
}
