package concurrency;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.FileProvider;
import domain.PaymentDomain;
import domain.PaymentDomainImpl;
import mapper.Mapper;
import xml.JAXBBuilder;
import xml.XmlException;
import xml.XmlParser;
import xml.elements.PaymentXml;

public class Producer implements Runnable {

	final static Logger logger = LoggerFactory.getLogger(Producer.class);

	private final Drop drop;
	private final Mapper mapper;
	private XmlParser parser;
	private FileStorageReadOnly fileStorage;
	private final FileProvider fileProvider;

	private Object fileLock = new Object();
	private Object sentLock = new Object();
	private Object fileStorageLock = new Object();

	public Producer(Drop drop, Mapper mapper, FileProvider fileProvider)
			throws Exception {
		this.fileProvider = fileProvider;
		this.drop = drop;
		this.mapper = mapper;
		try {
			// TODO: set ot out!
			parser = new JAXBBuilder().build();
		} catch (XmlException e) {
			logger.error("Parser build error", e);
			throw e;
		}
		logger.info("Producer created");
	}

	@Override
	public void run() {

		File tmpFile = null;
		List<PaymentXml> payments = null;
		while (!Thread.interrupted()) {
			// Get temp copy of file from readed directory
			File f = getNextFileFromStorage();
			
			tmpFile = getNextTmpFileAndDeleteReal(f);

			// Get Payments from file
			try {
				payments = getPayments(tmpFile);
			} catch (Exception e1) {
				logger.error("Parse error", e1);
			}
			fileProvider.close(tmpFile);

			if (payments == null || payments.isEmpty()) {
				continue;
			}

			// Set payments to drop
			setPaymentsToDrop(payments);
		}

		// Delete temp directory
//		if (destDirectory != watcher.) {
//			destDirectory.deleteOnExit();
//		}
	}

	private List<PaymentXml> getPayments(File file) throws Exception {
		// TODO read file and get payments.
		// I think need use stax and jaxb..
		// GEt one payment -> to Drop -> next payment.
		// If no payment -> remove file

		try {
			List<PaymentXml> payments = null;
			// synchronized (tmpFileReadLock) {
			payments = parser.parse(file);
			// }
			logger.info("Read {} payments", payments.size());
			return payments;
		} catch (FileNotFoundException | XmlException e) {
			logger.error("Parse error", e);
			e.printStackTrace();
			throw e;
		}

	}


	private File getNextFileFromStorage() {
		File f = null;
		synchronized (fileStorageLock) {
			while ((f = fileStorage.getNextFile())==null) {
				try {
					fileStorageLock.wait();
				} catch (InterruptedException e) {}
			}
			fileStorageLock.notifyAll();
		}
		return f;
	}
	
	private File getNextTmpFileAndDeleteReal(File f){
		File tmpFile = null;
		synchronized (fileLock) {
			tmpFile = fileProvider.copyToTempFile(f, true);
		}
		return tmpFile;
	}

	private void setPaymentsToDrop(List<PaymentXml> payments) {
		for (PaymentXml payment : payments) {
			PaymentDomain domainPayment = mapper.map(payment,
					PaymentDomainImpl.class);
			try {
				synchronized (sentLock) {
					while (!drop.setPayment(domainPayment)) {
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

}
