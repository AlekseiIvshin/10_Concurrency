package concurrency.consumer;

import concurrency.queuestorages.drop.DropGetter;
import dao.PaymentDAO;
import mapper.Mapper;

public interface ConsumerFactory {

	ConsumerFactory addDropStorage(DropGetter drop);
	ConsumerFactory addMapper(Mapper mapper);
	ConsumerFactory addPaymentDAO(PaymentDAO dao);
	Consumer createConsumer();
	
}
