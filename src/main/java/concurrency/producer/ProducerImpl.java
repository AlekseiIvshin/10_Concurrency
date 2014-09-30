package concurrency.producer;

import java.io.File;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.FileProvider;
import concurrency.quequestorages.Drop;
import concurrency.quequestorages.FileStorageReadOnly;
import domain.PaymentDomain;
import domain.PaymentDomainImpl;
import mapper.Mapper;
import xml.XmlException;
import xml.elements.PaymentXml;
import xml.provider.XmlProvider;

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
			XmlProvider xmlProvider, FileStorageReadOnly fileStorage) {
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
			// Get temp copy of file from readed directory
			File f = getNextFileFromStorage();

			File tmpFile = getNextTmpFileAndDeleteReal(f);

			try {
				xmlProvider.parse(tmpFile);
			} catch (FileNotFoundException e) {
				logger.error("File not founded", e);
				errors++;
				continue;
			} catch (XmlException e) {
				logger.error("Parse error", e);
				errors++;
				continue;
			}

			PaymentXml tmp = null;
			while ((tmp = xmlProvider.getNextPayment()) != null) {
				setPaymentsToDrop(map(tmp));
			}
			fileProvider.close(tmpFile);
		}
		fileProvider.close();
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
		try {
			synchronized (sentLock) {
				while (!drop.setPayment(payment)) {
					try {
						sentLock.wait();
					} catch (InterruptedException e) {
					}
				}
				sentLock.notifyAll();
			}

		} catch (InterruptedException e) {
			logger.error("Interrapted ", e);
		}
	}

}
