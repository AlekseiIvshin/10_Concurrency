package concurrency.queuestorages.drop;

import domain.PaymentDomain;

/**
 * Interface for getting payment from queue.
 * @author Aleksei_Ivshin
 *
 */
public interface DropGetter {

	/**
	 * Get next payment.
	 * @return next payment. if return null it means that queue is closed (or interrupted)
	 */
	PaymentDomain getPayment();
}
