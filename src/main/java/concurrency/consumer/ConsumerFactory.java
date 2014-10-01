package concurrency.consumer;

import concurrency.quequestorages.drop.DropGetter;
import dao.PaymentDAO;
import mapper.Mapper;

public interface ConsumerFactory {

	ConsumerFactory addDropStorage(DropGetter drop);
	ConsumerFactory addMapper(Mapper mapper);
	ConsumerFactory addPaymentDAO(PaymentDAO dao);
	Consumer createConsumer();
	
}
