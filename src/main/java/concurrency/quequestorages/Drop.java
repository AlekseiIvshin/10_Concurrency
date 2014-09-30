package concurrency.quequestorages;

import domain.PaymentDomain;

public interface Drop {

	PaymentDomain getPayment();

	boolean setPayment(PaymentDomain payment) throws InterruptedException;
}
