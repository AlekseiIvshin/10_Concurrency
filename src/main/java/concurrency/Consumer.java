package concurrency;

import javax.persistence.EntityExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.payment.PaymentDAO;
import dao.payment.PaymentEntity;
import dao.payment.PaymentEntity;
import domain.PaymentDomain;
import mapper.Mapper;

public class Consumer implements Runnable {

	final static Logger logger = LoggerFactory.getLogger(Consumer.class);
	
	private final Drop drop;
	private final Mapper mapper;
	private final PaymentDAO dao;
	private final static int waitFileTimeout = 1000;

	private Object getLock = new Object();
	private Object persistEnity = new Object();

	public Consumer(Drop drop, Mapper mapper, PaymentDAO dao) {
		this.drop = drop;
		this.mapper = mapper;
		this.dao = dao;
		logger.info("Consumer created");
	}

	public PaymentDomain getPaymentFromDrop() {
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

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			PaymentDomain domainPayment = getPaymentFromDrop();
			logger.info("Get payment from drop");
			if (domainPayment != null) {
				try{
					setPaymentToDataBase(domainPayment);
				} catch(EntityExistsException e){
					logger.info("DB info", e);
				}
			}
		}
	}

	private void setPaymentToDataBase(PaymentDomain paymentDomain) {

		// TODO ??? single resposonility ???
		// TODO ??? Use interface in mapper ???
		PaymentEntity payment = mapper.map(paymentDomain,
				PaymentEntity.class);
		// TODO ??? single resposonility ???
		if(!dao.add(payment)){
			throw new EntityExistsException("Enitiy already exists");
		}
	}
}
