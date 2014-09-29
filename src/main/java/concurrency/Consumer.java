package concurrency;

import xml.elements.PaymentXml;
import dao.payment.PaymentDAO;
import dao.payment.PaymentEntityImpl;
import domain.PaymentDomain;
import mapper.Mapper;

public class Consumer implements Runnable{

	private final Drop drop;
	private final Mapper mapper;
	private final PaymentDAO dao = null;

	public Consumer(Drop drop, Mapper mapper) {
		this.drop = drop;
		this.mapper = mapper;
	}

	public void getPayment() {
		PaymentDomain domainPayment = null;
		while ((domainPayment = drop.getPayment()) == null) {
			try{
				wait(); // ???
			} catch(InterruptedException e){
				// TODO: something
				
			}
		}
		notifyAll();

		// TODO ??? single resposonility ???
		// TODO ??? Use interface in mapper ???
		PaymentEntityImpl payment = mapper.map(domainPayment, PaymentEntityImpl.class);
		// TODO ??? single resposonility ???
		dao.add(payment);
	}

	@Override
	public void run() {
		getPayment();
	}
}
