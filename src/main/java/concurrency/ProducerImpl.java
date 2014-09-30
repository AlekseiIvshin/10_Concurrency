package concurrency;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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

public class ProducerImpl implements Producer {

	final static Logger logger = LoggerFactory.getLogger(ProducerImpl.class);

	private final Drop drop;
	private final Mapper mapper;
	private final XmlParser parser;
	private final FileStorageReadOnly fileStorage;
	private final FileProvider fileProvider;

	private Object fileLock = new Object();
	private Object sentLock = new Object();
	private Object fileStorageLock = new Object();

	public ProducerImpl(Drop drop, Mapper mapper, FileProvider fileProvider, XmlParser parser, FileStorageReadOnly fileStorage){
		this.fileProvider = fileProvider;
		this.drop = drop;
		this.mapper = mapper;
		this.parser = parser;
		this.fileStorage = fileStorage;
	}
	

	@Override
	public void run() {
		List<PaymentXml> payments = new ArrayList<PaymentXml>();
		while (!Thread.interrupted()) {
			// Get temp copy of file from readed directory
			File f = getNextFileFromStorage();
			
			File tmpFile = getNextTmpFileAndDeleteReal(f);

			// Get Payments from file
			try {
				payments.addAll(getPayments(tmpFile));
			} catch (Exception e1) {
				logger.error("Parse error", e1);
			}
			fileProvider.close(tmpFile);

			if (payments.isEmpty()) {
				continue;
			}

			
			// Set payments to drop
			setPaymentsToDrop(map(payments));
			payments.clear();
		}
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
		synchronized (fileLock) {
			return fileProvider.copyToTempFile(f, true);
		}
	}

	private List<PaymentDomain> map(List<PaymentXml> payments){
		List<PaymentDomain> result = new ArrayList<PaymentDomain>();
		for (PaymentXml payment : payments) {
			result.add( mapper.map(payment,
					PaymentDomainImpl.class));
		}
		return result;
	}
	
	private void setPaymentsToDrop(List<PaymentDomain> payments) {
		for (PaymentDomain payment : payments) {
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

}
