package app;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.hibernate.persister.internal.PersisterFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.beans.util.Cache.Kind;

import common.FileProvider;
import common.FileProviderImpl;
import sun.util.logging.resources.logging;
import xml.FactoryException;
import xml.JAXBBuilder;
import xml.JAXBParser;
import xml.XmlException;
import xml.XmlParser;
import xml.provider.JAXBProviderFactory;
import xml.provider.XmlProvider;
import xml.provider.XmlProviderFactory;
import mapper.Mapper;
import concurrency.Consumer;
import concurrency.FileWatcher;
import concurrency.producer.Producer;
import concurrency.producer.ProducerFactory;
import concurrency.producer.ProducerFactoryImpl;
import concurrency.producer.ProducerImpl;
import concurrency.quequestorages.Drop;
import concurrency.quequestorages.FileStorage;
import concurrency.quequestorages.FileStorageImpl;
import dao.PaymentDAOImpl;
import static java.nio.file.StandardWatchEventKinds.*;

public class AppService {

	final static Logger logger = LoggerFactory.getLogger(Consumer.class);

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
			executorService.execute(new FileWatcher(new File(
					"src\\test\\resources"), fileStorage));
			for (int i = 0; i < countOfProducers; i++) {
				Producer prod = prodFactory.createProducer();
				executorService.execute(prod);
			}

			for (int i = 0; i < countOfConsumers; i++) {
				executorService.execute(new Consumer(drop, mapper,
						new PaymentDAOImpl()));
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
}
