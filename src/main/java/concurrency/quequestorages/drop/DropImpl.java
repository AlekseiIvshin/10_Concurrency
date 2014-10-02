package concurrency.quequestorages.drop;

import domain.PaymentDomain;

import java.util.concurrent.*;

public class DropImpl implements Drop {

	private final BlockingQueue<PaymentDomain> queue;

	public DropImpl(int queueSize) {
		queue = new ArrayBlockingQueue<PaymentDomain>(queueSize);
	}

	public synchronized PaymentDomain getPayment() {
		PaymentDomain domain=null;
		while((domain = queue.poll())==null){
			try{
				wait();
			}catch(InterruptedException e){
				return null;
			}
		}
		notifyAll();
		return domain;
	}

	public synchronized boolean setPayment(PaymentDomain payment) {
		if (payment == null) {
			return true;
		}
		while(!queue.offer(payment)){
			try {
				wait();
			} catch (InterruptedException e) {
				return false;
			}
		}
		notifyAll();
		return true;
	}

}
