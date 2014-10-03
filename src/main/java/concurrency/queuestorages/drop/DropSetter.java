package concurrency.queuestorages.drop;

import domain.PaymentDomain;

/**
 * Interface for setting payments to queue.
 * @author Aleksei_Ivshin
 *
 */
public interface DropSetter {

	/**
	 * Set payment to queue.
	 * @param payment new payment.
	 * @return true - all right, false - queue was closed (or interrupted)
	 */
	boolean setPayment(PaymentDomain payment);
}
