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

	// private Object persistEnity = new Object();

	public ConsumerImpl(DropGetter drop, Mapper mapper, PaymentDAO dao)
			throws NullPointerException {
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
			try {
				transfer();
			} catch (Exception e) {
				logger.error("Transfer error", e);
				return;
			}
		}
	}

	public void transfer() {
		PaymentDomain domainPayment = getPaymentFromDrop();
		if (domainPayment == null) {
			return;
		}
		logger.debug("[Get][Payment] '{}' from [drop queque]",
				domainPayment.toString());

		PaymentEntity paymentEntity = map(domainPayment);
		logger.debug("[Map][Payment] '{}' -> '{}'", domainPayment,
				paymentEntity);
		setPaymentToDataBase(paymentEntity);

	}

	private PaymentDomain getPaymentFromDrop() {
		PaymentDomain domainPayment = null;
		synchronized (getLock) {
			while ((domainPayment = drop.getPayment()) == null) {
				try {
					getLock.wait(waitFileTimeout);
				} catch (InterruptedException e) {
					return null;
				}
			}
			getLock.notifyAll();
		}

		return domainPayment;
	}

	private PaymentEntity map(PaymentDomain payment) {
		return mapper.map(payment, PaymentEntity.class);
	}

	private void setPaymentToDataBase(PaymentEntity paymentEntity) {
		if (paymentEntity == null) {
			return;
		}
		try {
			if (dao.add(paymentEntity)) {
				logger.debug("[Set][Payment] '{}' to [data store]: SUCCESS",
						paymentEntity);
			} else {
				logger.debug(
						"[Set][Payment] '{}' to [data store]: ALREADY EXISTS",
						paymentEntity);
			}
		} catch (Exception e) {
			logger.error("[Set][Payment] to [data store]", e);
		}
	}
}
