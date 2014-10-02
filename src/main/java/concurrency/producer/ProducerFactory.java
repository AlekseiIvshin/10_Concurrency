package concurrency.producer;

import mapper.Mapper;
import xml.provider.XmlProviderFactory;

import common.FactoryException;
import common.FileProvider;

import concurrency.quequestorages.drop.DropSetter;
import concurrency.quequestorages.files.FileGetter;

public interface ProducerFactory {

	ProducerFactory setDropStorage(DropSetter drop);
	ProducerFactory setMapper(Mapper mapper);
	ProducerFactory setFileQuequeStorage(FileGetter fileStorage);
	ProducerFactory setXmlProviderFactory(XmlProviderFactory parser);
	ProducerFactory setFileProvider(FileProvider fileProvider);
	Producer createProducer() throws FactoryException;
	
}
