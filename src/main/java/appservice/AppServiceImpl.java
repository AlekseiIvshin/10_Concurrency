package appservice;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mapper.Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xml.provider.StreamProviderFactory;

import common.FactoryException;
import common.FileProvider;

import concurrency.FileWatcher;
import concurrency.consumer.ConsumerFactory;
import concurrency.consumer.ConsumerFactoryImpl;
import concurrency.consumer.ConsumerImpl;
import concurrency.producer.ProducerFactory;
import concurrency.producer.ProducerFactoryImpl;
import concurrency.quequestorages.drop.Drop;
import concurrency.quequestorages.files.FileStorage;
import dao.PaymentDAO;
import dao.PaymentDAOImpl;

public class AppServiceImpl implements AppService {

	final static Logger logger = LoggerFactory.getLogger(ConsumerImpl.class);

	private final Mapper mapper;
	private final ExecutorService executorService;
	private final Drop drop;
	private final FileStorage fileStorage;
	private final FileProvider fileProvider;
	private FileWatcher fileWatcher;

	// private final ConfigReader configReader = new ConfigReader();

	private final int producerCount;
	private final int consumerCount;

	public AppServiceImpl(Drop drop, Mapper mapper, FileStorage storage,
			FileProvider fileProvider, int producerCount, int consumerCount,
			File defaultSourceDirectory) throws IOException {
		executorService = Executors.newCachedThreadPool();
		this.drop = drop;
		fileStorage = storage;
		this.mapper = mapper;
		this.fileProvider = fileProvider;

		this.producerCount = producerCount;
		this.consumerCount = consumerCount;
		fileWatcher = initFileWatcher(defaultSourceDirectory);
	}

	public void addWatchingDirectory(String directory) throws IOException {

		fileWatcher.addDirectory(new File(directory));

	}

	public void startService() throws ServiceException {
		if (fileWatcher == null) {
			throw new ServiceException("File watcher not created");
		}

		ProducerFactory prodFactory = initProducerFactory();
		if (prodFactory == null) {
			throw new ServiceException("Producer factory not created");
		}
		ConsumerFactory consumerFactory = initConsumerFactory();
		if (consumerFactory == null) {
			throw new ServiceException("Consumer factory not created");
		}

		logger.info("Parameters initialized");
		executorService.execute(fileWatcher);

		for (int i = 0; i < producerCount; i++) {
			try {
				executorService.execute(prodFactory.createProducer());
			} catch (FactoryException e) {
				logger.error("Producer factory", e);
				stopService();
				return;
			}
		}
		logger.info("Executed {} producer(s)", producerCount);

		for (int i = 0; i < consumerCount; i++) {
			executorService.execute(consumerFactory.createConsumer());
		}
		logger.info("Executed {} consumer(s)", consumerCount);
		logger.info("Service started");
	}

	public void stopService() {
		logger.info("Service stopped");
		executorService.shutdown();
	}

	private ProducerFactory initProducerFactory() {
		ProducerFactory prodFactory = new ProducerFactoryImpl();

		return prodFactory.setDropStorage(drop).setFileProvider(fileProvider)
				.setFileQuequeStorage(fileStorage).setMapper(mapper)
				.setXmlProviderFactory(new StreamProviderFactory());
				//.setXmlProviderFactory(new JAXBProviderFactory());

	}

	private ConsumerFactory initConsumerFactory() {
		ConsumerFactory consumerFactory = new ConsumerFactoryImpl();
		PaymentDAO dao = new PaymentDAOImpl();
		return consumerFactory.addDropStorage(drop).addMapper(mapper)
				.addPaymentDAO(dao);
	}

	private FileWatcher initFileWatcher(File defaultSourceDirectory) {
		FileWatcher watcher = null;
		try {
			watcher = new FileWatcher(fileStorage);
		} catch (IOException e) {
			logger.error("Can't create file watcher", e);
			return null;
		}
		if (defaultSourceDirectory != null) {
			try {
				watcher.addDirectory(defaultSourceDirectory);
			} catch (IOException e) {
				logger.info("Add default source directory", e);
			}
		}
		return watcher;
	}

	@Override
	public Map<String, String> getConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}


}
