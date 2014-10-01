package concurrency.quequestorages;

import domain.PaymentDomain;

import java.util.concurrent.*;

public class DropImpl implements Drop {

	private final BlockingQueue<PaymentDomain> queue;

	public DropImpl(int queueSize) {
		queue = new ArrayBlockingQueue<PaymentDomain>(queueSize);
	}

	@Override
	public synchronized PaymentDomain getPayment() {
		return queue.poll();
	}

	@Override
	public synchronized boolean setPayment(PaymentDomain payment){
		return queue.offer(payment);
	}

}
