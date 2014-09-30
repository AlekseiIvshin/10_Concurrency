package concurrency.consumer;

import concurrency.quequestorages.Drop;
import dao.PaymentDAO;
import mapper.Mapper;

public class ConsumerFactoryImpl implements ConsumerFactory {

	private Drop drop;
	private Mapper mapper;
	private PaymentDAO dao;
	
	
	@Override
	public ConsumerFactory addDropStorage(Drop drop) {
		this.drop = drop;
		return this;
	}

	@Override
	public ConsumerFactory addMapper(Mapper mapper) {
		this.mapper = mapper;
		return this;
	}

	@Override
	public ConsumerFactory addPaymentDAO(PaymentDAO dao) {
		this.dao = dao;
		return this;
	}

	@Override
	public Consumer createConsumer() {
		return new ConsumerImpl(drop, mapper, dao);
	}



}
