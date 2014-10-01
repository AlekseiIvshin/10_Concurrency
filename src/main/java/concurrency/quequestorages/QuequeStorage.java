package concurrency.quequestorages;

public interface QuequeStorage<T> {

	void put(T item);
	T getNext();
}
