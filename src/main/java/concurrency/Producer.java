package concurrency;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import domain.PaymentDomain;
import mapper.Mapper;
import xml.JAXBBuilder;
import xml.JAXBParser;
import xml.XmlException;
import xml.XmlParser;
import xml.elements.PaymentXml;

public class Producer implements Runnable {

	final static Logger logger = LoggerFactory.getLogger(JAXBParser.class);

	private final Drop drop;
	private final Mapper mapper;
	private XmlParser parser;
	private File readedDirectory;
	
	private Object fileLock = new Object();

	public Producer(Drop drop, Mapper mapper, File directoryForRead) throws Exception {
		if(!directoryForRead.isDirectory()){
			throw new Exception("Argument is not directory");		
		}
		this.readedDirectory = directoryForRead;
		this.drop = drop;
		this.mapper = mapper;
		try {
			parser = new JAXBBuilder().build();
		} catch (XmlException e) {
			logger.error("Parser build error", e);
			throw e;
		}
	}

	public void setPayment() {
		File f = null;
		List<PaymentXml> payments = null;
		// TODO ???Will it working???
		synchronized (fileLock) {
			while ((f = getNextFile())==null) {
				try {
					wait();
				} catch (InterruptedException e) {}
			}
			notifyAll();
			
			try {
				payments = readPayment(f);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			f.delete();
		}
		
		if (payments != null) {
			for (PaymentXml payment : payments) {
				PaymentDomain domainPayment = mapper.map(payment,
						PaymentDomain.class);
				try {
					while (!drop.setPayment(domainPayment)) {
						try {
							wait();
						} catch (InterruptedException e) {
						}
					}
					notifyAll();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private List<PaymentXml> readPayment(File file) throws Exception {
		// TODO read file and get payments.
		// I think need use stax and jaxb..
		// GEt one payment -> to Drop -> next payment.
		// If no payment -> remove file
		try {
			parser.parse(file);
			return parser.getPayments();
		} catch (FileNotFoundException | XmlException e) {
			logger.error("Parse error", e);
			throw e;
		}

	}

	@Override
	public void run() {
		setPayment();
	}

	private File getNextFile() {
		
		
		
		File[] files = readedDirectory.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		});
		if(files.length>0){
			return files[0];
		}
		return null;
	}

}
