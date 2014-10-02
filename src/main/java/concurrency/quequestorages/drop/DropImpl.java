package concurrency.quequestorages.drop;

import domain.PaymentDomain;

import java.util.concurrent.*;

public class DropImpl implements Drop {

	private final BlockingQueue<PaymentDomain> queue;

	public DropImpl(int queueSize) {
		queue = new ArrayBlockingQueue<PaymentDomain>(queueSize);
	}

	public synchronized PaymentDomain getPayment() {
		return queue.poll();
	}

	public synchronized boolean setPayment(PaymentDomain payment) {
		if (payment == null) {
			return true;
		}
		return queue.offer(payment);
	}

}
