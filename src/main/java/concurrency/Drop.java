package concurrency;

import domain.PaymentDomain;

public interface Drop {

	PaymentDomain getPayment();

	boolean setPayment(PaymentDomain payment) throws InterruptedException;
}
