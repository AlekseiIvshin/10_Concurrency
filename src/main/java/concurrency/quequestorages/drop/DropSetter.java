package concurrency.quequestorages.drop;

import domain.PaymentDomain;

public interface DropSetter {

	boolean setPayment(PaymentDomain payment);
}
