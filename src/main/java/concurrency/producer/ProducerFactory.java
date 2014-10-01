package concurrency.producer;

import common.FactoryException;
import common.FileProvider;
import concurrency.quequestorages.drop.DropSetter;
import concurrency.quequestorages.files.FileStorageReadOnly;
import xml.provider.XmlProviderFactory;
import mapper.Mapper;

public interface ProducerFactory {

	ProducerFactory setDropStorage(DropSetter drop);
	ProducerFactory setMapper(Mapper mapper);
	ProducerFactory setFileQuequeStorage(FileStorageReadOnly fileStorage);
	ProducerFactory setXmlProviderFactory(XmlProviderFactory parser);
	ProducerFactory setFileProvider(FileProvider fileProvider);
	Producer createProducer() throws FactoryException;
	
}
