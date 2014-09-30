package concurrency.producer;

import common.FileProvider;
import concurrency.quequestorages.Drop;
import concurrency.quequestorages.FileStorageReadOnly;
import xml.provider.XmlProvider;
import mapper.Mapper;

public interface ProducerFactory {

	ProducerFactory addDropStorage(Drop drop);
	ProducerFactory addMapper(Mapper mapper);
	ProducerFactory addFileQuequeStorage(FileStorageReadOnly fileStorage);
	ProducerFactory addXmlProvider(XmlProvider parser);
	ProducerFactory addFileProvider(FileProvider fileProvider);
	Producer createProducer();
	
}
