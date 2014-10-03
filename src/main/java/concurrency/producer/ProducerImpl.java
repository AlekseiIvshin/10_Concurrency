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
import concurrency.queuestorages.drop.DropSetter;
import concurrency.queuestorages.files.FileGetter;
import domain.PaymentDomain;
import domain.PaymentDomainImpl;

public class ProducerImpl implements Producer {

	final static Logger logger = LoggerFactory.getLogger(ProducerImpl.class);

	private final DropSetter drop;
	private final Mapper mapper;
	private final XmlProvider xmlProvider;
	private final FileGetter fileStorage;
	private final FileProvider fileProvider;

	/**
	 * Lock threads then file preparing for further work.
	 */
	private Object fileLock = new Object();
	

	public ProducerImpl(DropSetter drop, Mapper mapper,
			FileProvider fileProvider, XmlProvider xmlProvider,
			FileGetter fileStorage) throws NullPointerException {
		
		String errorComponents = checkArgumentsAndReturnErrorText(drop, mapper, fileProvider, xmlProvider, fileStorage);
		if (errorComponents.length()>0) {
			throw new NullPointerException("Must be not null: "
					+ errorComponents);
		}
		this.fileProvider = fileProvider;
		this.drop = drop;
		this.mapper = mapper;
		this.xmlProvider = xmlProvider;
		this.fileStorage = fileStorage;
	}
	
	/**
	 * Check argument and create errors list.
	 * @param drop drop
	 * @param mapper mapper
	 * @param fileProvider file provider
	 * @param xmlProvider xml provider
	 * @param fileStorage file queue
	 * @return errors text list
	 */
	private String checkArgumentsAndReturnErrorText(DropSetter drop, Mapper mapper,
			FileProvider fileProvider, XmlProvider xmlProvider,
			FileGetter fileStorage){
		return (drop == null ? "Drop, " : "")
				+ (mapper == null ? "Mapper, " : "")
				+ (fileProvider == null ? "FileProvider, " : "")
				+ (fileStorage == null ? "FileStorage, " : "")
				+ (xmlProvider == null ? "XmlProvider" : "");
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				transfer();
			} catch (FileNotFoundException e) {
				logger.error("File not founded", e);
				continue;
			} catch (XmlException e) {
				logger.error("Parse error", e);
				continue;
			}
		}
		fileProvider.close();
	}

	/**
	 * Do One iteration: transfer payment file queue -> payment queue. 
	 * @throws FileNotFoundException
	 * @throws XmlException
	 */
	public void transfer() throws FileNotFoundException, XmlException {
		
		// Get file from queue
		File fileFromQueue = fileStorage.getNextFile();
		if (fileFromQueue == null) {
			return;
		}
		logger.debug("[Get][File] '{}' [file queue]", fileFromQueue.getName());
		
		//Prepare file for further work.
		File tmpFile = getPreparedFile(fileFromQueue);
		if (tmpFile == null) {
			return;
		}
		logger.debug(
				"[Prepare][File]: [Copy] source: '{}', destination: '{}' -> [Delete] source file",
				fileFromQueue, tmpFile.getName());
		
		logger.debug("[Parse][File] '{}'.[Use] provider '{}'", tmpFile.getName(),xmlProvider
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
				logger.debug("[Set][Payment] '{}' to [drop queue]:SUCCESS",paymentDomain);
			} else {
				logger.debug("[drop queue] is closed",paymentDomain);
				break;
			}
		}
		xmlProvider.close();
		fileProvider.close(tmpFile);
	}

	/**
	 * Prepare file.
	 * @param f source file
	 * @return result file
	 */
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

	/**
	 * Map payment from XML to domain object.
	 * @param payment XML payment
	 * @return domain payment
	 */
	private PaymentDomain map(PaymentXml payment) {
		return mapper.map(payment, PaymentDomainImpl.class);
	}


}
