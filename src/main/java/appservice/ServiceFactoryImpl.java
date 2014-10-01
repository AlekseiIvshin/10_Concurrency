package appservice;

import java.io.File;
import java.io.IOException;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;

import mapper.Mapper;
import mapper.MapperImpl;
import common.ConfigReader;
import common.FactoryException;
import common.FileProvider;
import common.FileProviderImpl;
import concurrency.quequestorages.drop.Drop;
import concurrency.quequestorages.drop.DropImpl;
import concurrency.quequestorages.files.FileStorage;
import concurrency.quequestorages.files.FileStorageImpl;

public class ServiceFactoryImpl implements ServiceFactory {

	private final ConfigReader configReader = new ConfigReader();
	
	
	private final String defaultTempPath = "tmp";
	private final int defaultProducerCount = 2;
	private final int defaultConsumerCount = 2;
	private final int defaultDropQuequeSize = 10;
	private final int defaultFileQuequeSize = 10;
	

	@Override
	public AppService createService() throws FactoryException {
		String destPath = "";
		try {
			destPath = configReader.getValue("temporaryDirectory");
		} catch (IOException e1) {
			destPath = defaultTempPath;
		}
		
		int prodCount = 0;
		try {
			prodCount = configReader.getIntValue("producerCount");
		} catch (IOException e) {
			prodCount = defaultProducerCount;
		}
		int consCount = 0;
		try {
			consCount = configReader.getIntValue("consumerCount");
		} catch (IOException e) {
			consCount = defaultConsumerCount;
		}
		
		int dropQueueSize;
		try {
			dropQueueSize = configReader.getIntValue("dropQuequeSize");
		} catch (IOException e1) {
			dropQueueSize = defaultDropQuequeSize;
		}
		int fileQueueSize;
		try {
			fileQueueSize = configReader.getIntValue("fileQueueSize");
		} catch (IOException e1) {
			fileQueueSize = defaultFileQuequeSize;
		}
		
		
		
		Mapper mapper = initMapper();
		Drop drop = new DropImpl(dropQueueSize);
		FileStorage fileStorage = new FileStorageImpl(fileQueueSize);
		FileProvider fileProvider =  
				new FileProviderImpl(new File(destPath));
		
		
		
		try {
			return new AppServiceImpl(drop, mapper, fileStorage,fileProvider,prodCount, consCount);
		} catch (IOException e) {
			throw new FactoryException(e.getMessage());
		}
	}
	
	
	public Mapper initMapper(){
		return new MapperImpl();
	}
	

	

}
