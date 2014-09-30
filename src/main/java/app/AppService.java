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
import xml.JAXBBuilder;
import xml.JAXBParser;
import xml.XmlException;
import xml.XmlParser;
import mapper.Mapper;
import concurrency.Consumer;
import concurrency.Drop;
import concurrency.FileStorage;
import concurrency.FileStorageImpl;
import concurrency.FileWatcher;
import concurrency.Producer;
import concurrency.ProducerFactory;
import concurrency.ProducerFactoryImpl;
import concurrency.ProducerImpl;
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
			ProducerFactory prodFactory = initProducerFactory();
			if(prodFactory == null){
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

	private ProducerFactory initProducerFactory() {
		ProducerFactory prodFactory = new ProducerFactoryImpl();
		XmlParser parser;
		try {
			parser = new JAXBBuilder().build();
		} catch (XmlException e) {
			logger.error(
					"Can not create producer factory: xml parser builder error",
					e);
			return null;
		}
		return prodFactory.addDropStorage(drop).addFileProvider(fileProvider)
				.addFileQuequeStorage(fileStorage).addMapper(mapper)
				.addXmlParser(parser);

	}
}
