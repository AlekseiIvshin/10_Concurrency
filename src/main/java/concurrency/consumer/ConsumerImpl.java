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

	public ConsumerImpl(DropGetter drop, Mapper mapper, PaymentDAO dao) {
		this.drop = drop;
		this.mapper = mapper;
		this.dao = dao;
		logger.info("Consumer created");
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			PaymentDomain domainPayment = getPaymentFromDrop();
			logger.info("Get payment from drop");
			if (domainPayment != null) {
				try {
					setPaymentToDataBase(map(domainPayment));
				} catch (EntityExistsException e) {
					logger.info("DB info", e);
				}
			}
		}
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
	
	private PaymentEntity map(PaymentDomain payment){
		return mapper.map(payment, PaymentEntity.class);
	}

	private void setPaymentToDataBase(PaymentEntity paymentEntity) {

		// TODO ??? single resposonility ???
		if (!dao.add(paymentEntity)) {
			throw new EntityExistsException("Enitiy already exists");
		}
	}
}
