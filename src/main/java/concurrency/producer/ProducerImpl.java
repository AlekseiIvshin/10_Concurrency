package concurrency.producer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import mapper.Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xml.elements.PaymentXml;
import xml.provider.XmlProvider;

import common.FileProvider;
import common.XmlException;

import concurrency.quequestorages.drop.DropSetter;
import concurrency.quequestorages.files.FileStorageReadOnly;
import domain.PaymentDomain;
import domain.PaymentDomainImpl;

public class ProducerImpl implements Producer {

	final static Logger logger = LoggerFactory.getLogger(ProducerImpl.class);

	private final DropSetter drop;
	private final Mapper mapper;
	private final XmlProvider xmlProvider;
	private final FileStorageReadOnly fileStorage;
	private final FileProvider fileProvider;

	private Object fileLock = new Object();
	private int errors;

	public ProducerImpl(DropSetter drop, Mapper mapper,
			FileProvider fileProvider, XmlProvider xmlProvider,
			FileStorageReadOnly fileStorage) throws NullPointerException {
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
		File fileFromQueque = fileStorage.getNextFile();
		if (fileFromQueque == null) {
			return;
		}
		logger.debug("[Get][File] '{}' [file queque]", fileFromQueque.getName());

		File tmpFile = getPreparedFile(fileFromQueque);
		if (tmpFile == null) {
			return;
		}
		logger.debug(
				"[Prepare][File]: [Copy] source: '{}', destination: '{}' -> [Delete] source file",
				fileFromQueque, tmpFile.getName());
		logger.debug("[Parse][File]: [Use] provider '{}'", xmlProvider
				.getClass().getName());
		xmlProvider.parse(tmpFile);

		PaymentXml nextPayment = null;
		while ((nextPayment = xmlProvider.getNextPayment()) != null) {
			logger.debug("[Get][Payment] '{}' from '{}'", nextPayment,
					tmpFile.getName());
			PaymentDomain paymentDomain = map(nextPayment);
			logger.debug("[Map][Payment] '{}' -> '{}'", nextPayment,
					paymentDomain);
			if(drop.setPayment(paymentDomain)){
				logger.debug("[Set][Payment] '{}' to [drop queque]:SUCCESS",paymentDomain);
			} else {
				logger.debug("[drop queque] is closed",paymentDomain);
				break;
			}
		}
		xmlProvider.close();
		fileProvider.close(tmpFile);
	}

	public int getErrorCount() {
		return errors;
	}

	private File getPreparedFile(File f) {
		synchronized (fileLock) {
			try {
				return fileProvider.prepareFile(f);
			} catch (IOException e) {
				logger.error("Prepare file error", e);
				return null;
			}
		}
	}

	private PaymentDomain map(PaymentXml payment) {
		return mapper.map(payment, PaymentDomainImpl.class);
	}


}
