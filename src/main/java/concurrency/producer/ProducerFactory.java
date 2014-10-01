package concurrency.producer;

import common.FactoryException;
import common.FileProvider;
import concurrency.quequestorages.Drop;
import concurrency.quequestorages.FileStorageReadOnly;
import xml.provider.XmlProvider;
import mapper.Mapper;

public interface ProducerFactory {

	ProducerFactory setDropStorage(Drop drop);
	ProducerFactory setMapper(Mapper mapper);
	ProducerFactory setFileQuequeStorage(FileStorageReadOnly fileStorage);
	ProducerFactory setXmlProvider(XmlProvider parser);
	ProducerFactory setFileProvider(FileProvider fileProvider);
	Producer createProducer() throws FactoryException;
	
}
