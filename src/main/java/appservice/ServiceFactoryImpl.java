package appservice;

import java.io.File;
import java.io.IOException;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;

import com.sun.org.apache.regexp.internal.recompile;

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

	private final String destPath;
	private final int prodCount;
	private final int consCount;
	private final int dropQueueSize;
	private final int fileQueueSize;
	private final File defaultSourceDirctory;

	public ServiceFactoryImpl() {
		destPath = getDestinationPath();
		prodCount = getProducerCount();
		consCount = getConsumerCount();
		dropQueueSize = getDropQuequeSize();
		fileQueueSize = getFileQuequeSize();
		defaultSourceDirctory = getDefaultSourceDirectory();
	}

	@Override
	public AppService createService() throws FactoryException {

		Mapper mapper = new MapperImpl();
		Drop drop = new DropImpl(dropQueueSize);
		FileStorage fileStorage = new FileStorageImpl(fileQueueSize);
		FileProvider fileProvider = new FileProviderImpl(new File(destPath));

		try {
			return new AppServiceImpl(drop, mapper, fileStorage, fileProvider,
					prodCount, consCount, defaultSourceDirctory);
		} catch (IOException e) {
			throw new FactoryException(e.getMessage());
		}
	}

	private int getDropQuequeSize() {
		try {
			return configReader.getIntValue("dropQuequeSize");
		} catch (IOException e1) {
			return defaultDropQuequeSize;
		}
	}

	private int getFileQuequeSize() {
		try {
			return configReader.getIntValue("fileQueueSize");
		} catch (IOException e1) {
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

	@Override
	public String getInitInfo() {
		return "[Producer count = "+prodCount+"]"
				+ "[Consumer count = "+consCount+"]"
				+ "[Drop queque size = "+dropQueueSize+"]"
				+ "[File queque size = "+fileQueueSize+"]"
				+ "[Default source directory = '"+defaultSourceDirctory+"']"
				+ "[Destination directory = '"+destPath+"']";
	}

}
