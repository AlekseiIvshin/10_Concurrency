package app;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.FileProvider;
import common.FileProviderImpl;
import xml.FactoryException;
import xml.provider.JAXBProviderFactory;
import xml.provider.XmlProvider;
import xml.provider.XmlProviderFactory;
import mapper.Mapper;
import concurrency.FileWatcher;
import concurrency.consumer.ConsumerFactory;
import concurrency.consumer.ConsumerFactoryImpl;
import concurrency.consumer.ConsumerImpl;
import concurrency.producer.Producer;
import concurrency.producer.ProducerFactory;
import concurrency.producer.ProducerFactoryImpl;
import concurrency.quequestorages.Drop;
import concurrency.quequestorages.FileStorage;
import dao.PaymentDAO;
import dao.PaymentDAOImpl;
import static java.nio.file.StandardWatchEventKinds.*;

public class AppService {

	final static Logger logger = LoggerFactory.getLogger(ConsumerImpl.class);

	private final Mapper mapper;
	private final ExecutorService executorService;
	private final Drop drop;
	private final FileStorage fileStorage;
	private final WatchService watcher;
	private final FileProvider fileProvider;

	public AppService(Drop drop, Mapper mapper, FileStorage storage)
			throws ServiceException {
		executorService = Executors.newCachedThreadPool();
		this.drop = drop;
		fileStorage = storage;
		this.mapper = mapper;
		fileProvider = new FileProviderImpl(new File(
				"src\\test\\resources\\temp"));
		try {
			watcher = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			logger.error("Can't create directory watcher", e);
			throw new ServiceException("Can't create directory watcher: "
					+ e.getMessage());
		}
	}

	public void addWatchDirectory(String directory) throws IOException {
		File f = new File(directory);
		if (!f.isDirectory()) {
			throw new IOException(directory
					+ " is not readable or not directory");
		}
		Path dir = f.toPath();
		try {
			dir.register(watcher, ENTRY_CREATE);
		} catch (IOException e) {
			logger.error("Watch key not registred", e);
			throw e;
		}

	}

	public void startService(File directory, int countOfProducers,
			int countOfConsumers) {
		try {
			if (countOfProducers <= 0 || countOfConsumers <= 0) {
				throw new Exception(
						" producers and consumers count must be greater then 0");
			}
			ProducerFactory prodFactory = null;
			try {
				prodFactory = initProducerFactory();
			} catch (FactoryException e) {
				logger.error("Factory can't create producer", e);
				logger.info("Service stopped");
				return;
			}
			ConsumerFactory consumerFactory = initConsumerFactory();
			
			executorService.execute(new FileWatcher(new File(
					"src\\test\\resources"), fileStorage));
			
			for (int i = 0; i < countOfProducers; i++) {
				executorService.execute(prodFactory.createProducer());
			}

			for (int i = 0; i < countOfConsumers; i++) {
				executorService.execute(consumerFactory.createConsumer());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stopService() {
		executorService.shutdown();
	}

	private ProducerFactory initProducerFactory() throws FactoryException {
		ProducerFactory prodFactory = new ProducerFactoryImpl();
		XmlProviderFactory factory = new JAXBProviderFactory();
		XmlProvider provider = factory.createProvider();
		return prodFactory.addDropStorage(drop).addFileProvider(fileProvider)
				.addFileQuequeStorage(fileStorage).addMapper(mapper)
				.addXmlProvider(provider);

	}
	
	private ConsumerFactory initConsumerFactory(){
		ConsumerFactory consumerFactory = new ConsumerFactoryImpl();
		PaymentDAO dao = new PaymentDAOImpl();
		return consumerFactory.addDropStorage(drop).addMapper(mapper).addPaymentDAO(dao);
	}

}
