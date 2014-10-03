package appservice;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.exception.FactoryException;
import common.exception.ServiceException;
import concurrency.consumer.ConsumerFactory;
import concurrency.filewatcher.FileWatcher;
import concurrency.producer.ProducerFactory;

public class AppServiceImpl implements AppService {

	final static Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);

	private final ExecutorService executorService;
	private final FileWatcher fileWatcher;
	private final ConsumerFactory consumerFactory;
	private final ProducerFactory producerFactory;

	private final int producerCount;
	private final int consumerCount;

	public AppServiceImpl(ConsumerFactory consumerFactory,
			ProducerFactory producerFactory, FileWatcher fileWatcher,
			int producerCount, int consumerCount) throws IOException {
		executorService = Executors.newCachedThreadPool();
		this.producerCount = producerCount;
		this.consumerCount = consumerCount;
		this.fileWatcher = fileWatcher;
		this.consumerFactory = consumerFactory;
		this.producerFactory = producerFactory;
	}

	public void addWatchingDirectory(String directory) throws IOException {
		fileWatcher.addDirectory(new File(directory));
	}

	public void startService() throws ServiceException {
		if (fileWatcher == null) {
			throw new ServiceException("File watcher not created");
		}

		logger.info("Parameters initialized");
		executorService.execute(fileWatcher);

		for (int i = 0; i < producerCount; i++) {
			try {
				executorService.execute(producerFactory.createProducer());
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

}
