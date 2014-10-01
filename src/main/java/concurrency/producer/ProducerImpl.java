package concurrency.producer;

import java.io.File;
import java.io.FileNotFoundException;

import mapper.Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xml.elements.PaymentXml;
import xml.provider.XmlProvider;

import common.FileProvider;
import common.XmlException;

import concurrency.quequestorages.Drop;
import concurrency.quequestorages.FileStorageReadOnly;
import domain.PaymentDomain;
import domain.PaymentDomainImpl;

public class ProducerImpl implements Producer {

	final static Logger logger = LoggerFactory.getLogger(ProducerImpl.class);

	private final Drop drop;
	private final Mapper mapper;
	private final XmlProvider xmlProvider;
	private final FileStorageReadOnly fileStorage;
	private final FileProvider fileProvider;

	private Object fileLock = new Object();
	private Object sentLock = new Object();
	private Object fileStorageLock = new Object();

	private int errors;

	public ProducerImpl(Drop drop, Mapper mapper, FileProvider fileProvider,
			XmlProvider xmlProvider, FileStorageReadOnly fileStorage)
			throws NullPointerException {
		if (drop == null || mapper == null || fileProvider == null
				|| xmlProvider == null || fileStorage == null) {
			String errorComponents = (drop == null ? "Drop, " : "")
					+ (mapper == null ? "Mapper, " : "")
					+ (fileProvider == null ? "FileProvider, " : "")
					+ (fileStorage == null ? "FileStorage, " : "")
					+ (xmlProvider == null ? "XmlProvider" : "");
			throw new NullPointerException("Must be not null: "
					+ errorComponents);
		}
		this.fileProvider = fileProvider;
		this.drop = drop;
		this.mapper = mapper;
		this.xmlProvider = xmlProvider;
		this.fileStorage = fileStorage;
		errors = 0;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				transfer();
			} catch (FileNotFoundException e) {
				logger.error("File not founded", e);
				errors++;
				continue;
			} catch (XmlException e) {
				logger.error("Parse error", e);
				errors++;
				continue;
			}
		}
		fileProvider.close();
	}

	public void transfer() throws FileNotFoundException, XmlException {
		// Get temp copy of file from readed directory
		File fileFromQueque = getNextFileFromStorage();
		File tmpFile = getNextTmpFileAndDeleteReal(fileFromQueque);

		xmlProvider.parse(tmpFile);

		PaymentXml nextPayment = null;
		while ((nextPayment = xmlProvider.getNextPayment()) != null) {
			setPaymentsToDrop(map(nextPayment));
		}
		fileProvider.close(tmpFile);
	}

	public int getErrorCount() {
		return errors;
	}

	private File getNextFileFromStorage() {
		File f = null;
		synchronized (fileStorageLock) {
			while ((f = fileStorage.getNextFile()) == null) {
				try {
					fileStorageLock.wait();
				} catch (InterruptedException e) {
					// TODO: 
				}
			}
			fileStorageLock.notifyAll();
		}
		return f;
	}

	private File getNextTmpFileAndDeleteReal(File f) {
		synchronized (fileLock) {
			return fileProvider.copyToTempFile(f, true);
		}
	}

	private PaymentDomain map(PaymentXml payment) {
		return mapper.map(payment, PaymentDomainImpl.class);
	}

	private void setPaymentsToDrop(PaymentDomain payment) {
		synchronized (sentLock) {
			while (!drop.setPayment(payment)) {
				try {
					sentLock.wait();
				} catch (InterruptedException e) {
					// TODO: 
				}
			}
			sentLock.notifyAll();
		}
	}

}
