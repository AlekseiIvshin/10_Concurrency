package concurrency.consumer;

import javax.persistence.EntityExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import concurrency.quequestorages.drop.DropGetter;
import dao.PaymentDAO;
import dao.entities.PaymentEntity;
import domain.PaymentDomain;
import mapper.Mapper;

public class ConsumerImpl implements Consumer {

	final static Logger logger = LoggerFactory.getLogger(ConsumerImpl.class);

	private final DropGetter drop;
	private final Mapper mapper;
	private final PaymentDAO dao;
	private final static int waitFileTimeout = 1000;

	private Object getLock = new Object();
	//private Object persistEnity = new Object();

	public ConsumerImpl(DropGetter drop, Mapper mapper, PaymentDAO dao) 
			throws NullPointerException{
		if (drop == null || mapper == null || dao == null) {
			String errorComponents = (drop == null ? "Drop, " : "")
					+ (mapper == null ? "Mapper, " : "")
					+ (dao == null ? "PaymentDAO, " : "");
			throw new NullPointerException("Must be not null: "
					+ errorComponents);
		}
		this.drop = drop;
		this.mapper = mapper;
		this.dao = dao;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try{
				transfer();
			}catch(Exception e){
				logger.error("Transfer error", e);
				return;
			}
		}
	}
	
	public void transfer(){
		PaymentDomain domainPayment = getPaymentFromDrop();
		if (domainPayment != null) {
			setPaymentToDataBase(map(domainPayment));
		}
	}

	private PaymentDomain getPaymentFromDrop() {
		PaymentDomain domainPayment = null;
		synchronized (getLock) {
			while ((domainPayment = drop.getPayment()) == null) {
				try {
					getLock.wait(waitFileTimeout);
				} catch (InterruptedException e) {
				}
			}
			getLock.notifyAll();
		}

		return domainPayment;
	}
	
	private PaymentEntity map(PaymentDomain payment){
		return mapper.map(payment, PaymentEntity.class);
	}

	private void setPaymentToDataBase(PaymentEntity paymentEntity) {
		try{
			dao.add(paymentEntity);
		} catch(EntityExistsException e){ }
	}
}
