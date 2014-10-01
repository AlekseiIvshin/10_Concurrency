package concurrency.consumer;

import mapper.Mapper;
import concurrency.quequestorages.drop.DropGetter;
import dao.PaymentDAO;

public class ConsumerFactoryImpl implements ConsumerFactory {

	private DropGetter drop;
	private Mapper mapper;
	private PaymentDAO dao;
	
	
	@Override
	public ConsumerFactory addDropStorage(DropGetter drop) {
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
