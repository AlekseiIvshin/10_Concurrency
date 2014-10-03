package appservice;

import java.io.File;
import java.io.IOException;

import mapper.Mapper;
import mapper.MapperImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xml.provider.StreamProviderFactory;
import xml.provider.XmlProviderFactory;

import common.ConfigReader;
import common.exception.FactoryException;
import common.fileprovider.FileProviderFactory;
import common.fileprovider.FileProviderFactoryImpl;

import concurrency.consumer.ConsumerFactory;
import concurrency.consumer.ConsumerFactoryImpl;
import concurrency.filewatcher.FileWatcher;
import concurrency.filewatcher.FileWatcherImpl;
import concurrency.producer.ProducerFactory;
import concurrency.producer.ProducerFactoryImpl;
import concurrency.queuestorages.drop.Drop;
import concurrency.queuestorages.drop.DropImpl;
import concurrency.queuestorages.files.FileStorage;
import concurrency.queuestorages.files.FileStorageImpl;
import dao.PaymentDAO;
import dao.PaymentDAOImpl;

/**
 * Factory for creating service. Read 'config.properties'.
 * 
 * @author Aleksei_Ivshin
 *
 */
public class ServiceFactoryImpl implements ServiceFactory {

	final static Logger logger = LoggerFactory
			.getLogger(ServiceFactoryImpl.class);

	private final ConfigReader configReader;

	private final String defaultTempPath = "tmp";
	private final int defaultProducerCount = 2;
	private final int defaultConsumerCount = 2;
	private final int defaultDropQuequeSize = 10;
	private final int defaultFileQuequeSize = 10;

	private final int prodCount;
	private final int consCount;
	private final int dropQueueSize;
	private final int fileQueueSize;
	private final File defaultSourceDirectory;

	public ServiceFactoryImpl() {
		configReader = new ConfigReader();
		prodCount = getProducerCount();
		consCount = getConsumerCount();
		dropQueueSize = getDropQuequeSize();
		fileQueueSize = getFileQuequeSize();
		defaultSourceDirectory = getDefaultSourceDirectory();
	}

	@Override
	public AppService createService() throws FactoryException {

		Mapper mapper = new MapperImpl();
		Drop drop = new DropImpl(dropQueueSize);
		FileStorage fileStorage = new FileStorageImpl(fileQueueSize);

		FileProviderFactory fileProvider = getFileProviderFactory();
		XmlProviderFactory xmpProviderFactory = getXmlProviderFactory();
		PaymentDAO dao = new PaymentDAOImpl();
		FileWatcher fileWatcher = getFileWatcher(fileStorage,
				defaultSourceDirectory);
		ConsumerFactory consumerFactory = getConsumerFactory(drop, mapper, dao);
		ProducerFactory producerFactory = getProducerFactory(drop, fileStorage,
				xmpProviderFactory, fileProvider, mapper);

		try {
			return new AppServiceImpl(consumerFactory, producerFactory,
					fileWatcher, prodCount, consCount);
		} catch (IOException e) {
			throw new FactoryException(e.getMessage());
		}
	}

	@Override
	public String getInitInfo() {
		return "[Producer count = " + prodCount + "]" + "[Consumer count = "
				+ consCount + "]" + "[Drop queque size = " + dropQueueSize
				+ "]" + "[File queque size = " + fileQueueSize + "]"
				+ "[Default source directory = '" + defaultSourceDirectory
				+ "']";
	}

	private int getDropQuequeSize() {
		try {
			return configReader.getIntValue("dropQuequeSize");
		} catch (Exception e1) {
			return defaultDropQuequeSize;
		}
	}

	private int getFileQuequeSize() {
		try {
			return configReader.getIntValue("fileQueueSize");
		} catch (Exception e1) {
			return defaultFileQuequeSize;
		}
	}

	private File getDefaultSourceDirectory() {
		try {
			String defaultSourceDirctoryName = configReader
					.getValue("sourceDirectory");
			File defaultSourceDirctory = new File(defaultSourceDirctoryName);
			if (defaultSourceDirctory.exists()
					&& defaultSourceDirctory.isDirectory()) {
				return defaultSourceDirctory;
			}
			return null;
		} catch (IOException e1) {
			return null;
		}
	}

	private String getDestinationPath() {
		try {
			return configReader.getValue("temporaryDirectory");
		} catch (IOException e1) {
			return defaultTempPath;
		}
	}

	private int getProducerCount() {
		try {
			return configReader.getIntValue("producerCount");
		} catch (IOException e) {
			return defaultProducerCount;
		}
	}

	private int getConsumerCount() {
		try {
			return configReader.getIntValue("consumerCount");
		} catch (IOException e) {
			return defaultConsumerCount;
		}
	}

	private FileProviderFactory getFileProviderFactory() {
		return new FileProviderFactoryImpl(new File(getDestinationPath()));
	}

	private XmlProviderFactory getXmlProviderFactory() {
		return new StreamProviderFactory();
	}

	private ConsumerFactory getConsumerFactory(Drop drop, Mapper mapper,
			PaymentDAO dao) {
		ConsumerFactory consumerFactory = new ConsumerFactoryImpl();
		consumerFactory.addDropStorage(drop);
		consumerFactory.addMapper(mapper);
		consumerFactory.addPaymentDAO(dao);
		return consumerFactory;
	}

	private ProducerFactory getProducerFactory(Drop drop,
			FileStorage fileStorage, XmlProviderFactory xmlProviderFactory,
			FileProviderFactory fileProviderFactory, Mapper mapper) {
		ProducerFactory factory = new ProducerFactoryImpl();
		factory.setDropStorage(drop);
		factory.setFileProviderFactory(fileProviderFactory);
		factory.setFileQueueStorage(fileStorage);
		factory.setMapper(mapper);
		factory.setXmlProviderFactory(xmlProviderFactory);
		return factory;
	}

	private FileWatcher getFileWatcher(FileStorage fileStorage,
			File defaultSourceDirectory) {
		FileWatcher watcher = null;
		try {
			watcher = new FileWatcherImpl(fileStorage);
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
}
