package appservice;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.FactoryException;
import common.FileProvider;
import xml.provider.JAXBProviderFactory;
import xml.provider.XmlProvider;
import xml.provider.XmlProviderFactory;
import mapper.Mapper;
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
import static java.nio.file.StandardWatchEventKinds.*;

public class AppServiceImpl implements AppService {

	final static Logger logger = LoggerFactory.getLogger(ConsumerImpl.class);
	


	private final Mapper mapper;
	private final ExecutorService executorService;
	private final Drop drop;
	private final FileStorage fileStorage;
	private final WatchService watcher;
	private final FileProvider fileProvider;
	private Set<File> watchigDirectories;
	

	//private final ConfigReader configReader = new ConfigReader();


	private final int producerCount;
	private final int consumerCount;

	public AppServiceImpl(Drop drop, Mapper mapper, FileStorage storage, FileProvider fileProvider, int producerCount, int consumerCount)
			throws IOException {
		executorService = Executors.newCachedThreadPool();
		this.drop = drop;
		fileStorage = storage;
		this.mapper = mapper;
		this.fileProvider = fileProvider;
//				new FileProviderImpl(new File(
//				"src\\test\\resources\\temp"));
		watcher = FileSystems.getDefault().newWatchService();
		
		watchigDirectories = new HashSet<File>();
		this.producerCount = producerCount;
		this.consumerCount= consumerCount;
	}

	public void addWatchingDirectory(String directory) throws IOException {
		if (watchigDirectories.contains(directory)) {
			return;
		}
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
		watchigDirectories.add(f);

	}

	public void startService() throws ServiceException {
			if (watchigDirectories.isEmpty()) {
				throw new ServiceException("List directories for watching is empty");
			} 
			
			ProducerFactory prodFactory = initProducerFactory();
			if(prodFactory == null){
				throw new ServiceException("Producer factory not created");
			}
			ConsumerFactory consumerFactory = initConsumerFactory();
			if(consumerFactory == null){
				throw new ServiceException("Consumer factory not created");
			}
			FileWatcher fileWatcher = initFileWatcher();
			if(fileWatcher == null){
				throw new ServiceException("File watcher not created");
			}
			
			
			logger.info("Parameters initialized");
			executorService.execute(fileWatcher);

			for (int i = 0; i < producerCount; i++) {
				try {
					executorService.execute(prodFactory.createProducer());
				} catch (FactoryException e) {
					logger.error("Producer factory",e);
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

	private ProducerFactory initProducerFactory(){
		ProducerFactory prodFactory = new ProducerFactoryImpl();
		XmlProviderFactory factory = new JAXBProviderFactory();
		XmlProvider provider;
		try {
			provider = factory.createProvider();
		} catch (FactoryException e) {
			logger.error("Factory can't create producer", e);
			return null;
		}
		return prodFactory.setDropStorage(drop).setFileProvider(fileProvider)
				.setFileQuequeStorage(fileStorage).setMapper(mapper)
				.setXmlProvider(provider);

	}

	private ConsumerFactory initConsumerFactory() {
		ConsumerFactory consumerFactory = new ConsumerFactoryImpl();
		PaymentDAO dao = new PaymentDAOImpl();
		return consumerFactory.addDropStorage(drop).addMapper(mapper)
				.addPaymentDAO(dao);
	}
	
	private FileWatcher initFileWatcher(){
		try {
			return new FileWatcher(new File(
					"src\\test\\resources"), fileStorage);
		} catch (IOException e) {
			logger.error("Can't create file watcher",e);
			return null;
		}
	}

	@Override
	public Map<String, String> getConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
