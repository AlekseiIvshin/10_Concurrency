package concurrency;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.PaymentDomain;
import domain.PaymentDomainImpl;
import mapper.Mapper;
import xml.FileUtils;
import xml.JAXBBuilder;
import xml.XmlException;
import xml.XmlParser;
import xml.elements.PaymentXml;

public class Producer implements Runnable {

	final static Logger logger = LoggerFactory.getLogger(Producer.class);

	private final Drop drop;
	private final Mapper mapper;
	private XmlParser parser;
	private File readedDirectory;
	private File destDirectory;
	private final static int waitFileTimeout = 1000;

	private Object fileLock = new Object();
	private Object sentLock = new Object();

	public Producer(Drop drop, Mapper mapper, File directoryForRead)
			throws Exception {
		if (!directoryForRead.isDirectory()) {
			throw new Exception("Argument is not directory");
		}
		this.readedDirectory = directoryForRead;
		this.drop = drop;
		this.mapper = mapper;
		try {
			// TODO: set ot out!
			parser = new JAXBBuilder().build();
		} catch (XmlException e) {
			logger.error("Parser build error", e);
			throw e;
		}
		destDirectory = readedDirectory;
		logger.info("Producer created");
	}

	public void setDestDirectory(File destDirectory) {
		this.destDirectory = destDirectory;
	}

	@Override
	public void run() {

		File tmpFile = null;
		List<PaymentXml> payments = null;
		while (!Thread.interrupted()) {
			// Get temp copy of file from readed directory
			tmpFile = getNextFile();

			// Get Payments from file
			try {
				payments = getPayments(tmpFile);
			} catch (Exception e1) {
				logger.error("Parse error", e1);
			}
			tmpFile.delete();

			if (payments == null || payments.isEmpty()) {
				continue;
			}

			// Set payments to drop
			setPaymentsToDrop(payments);
		}

		// Delete temp directory
		if (destDirectory != readedDirectory) {
			destDirectory.deleteOnExit();
		}
	}

	private List<PaymentXml> getPayments(File file) throws Exception {
		// TODO read file and get payments.
		// I think need use stax and jaxb..
		// GEt one payment -> to Drop -> next payment.
		// If no payment -> remove file

		try {
			List<PaymentXml> payments = null;
			//synchronized (tmpFileReadLock) {
				payments = parser.parse(file);
			//}
			logger.info("Read {} payments", payments.size());
			return payments;
		} catch (FileNotFoundException | XmlException e) {
			logger.error("Parse error", e);
			e.printStackTrace();
			throw e;
		}

	}

	private File getNextFile() {
		File tmpFile = null;
		synchronized (fileLock) {
			while ((tmpFile = FileUtils.getNextTmpFile(readedDirectory,
					destDirectory, true)) == null) {
				try {
					fileLock.wait(waitFileTimeout);
				} catch (InterruptedException e) {
				}
			}
			fileLock.notifyAll();
		}
		return tmpFile;
	}
	
	private void setPaymentsToDrop(List<PaymentXml> payments){
		for (PaymentXml payment : payments) {
			PaymentDomain domainPayment = mapper.map(payment,
					PaymentDomainImpl.class);
			try {
				synchronized (sentLock) {
					while (!drop.setPayment(domainPayment)) {
						try {
							sentLock.wait();
						} catch (InterruptedException e) {}
					}
					sentLock.notifyAll();
				}

			} catch (InterruptedException e) {
				logger.error("Interrapted ", e);
			}
		}
	}

}
