package concurrency;

import common.FileProvider;

import xml.XmlParser;
import mapper.Mapper;

public interface ProducerFactory {

	ProducerFactory addDropStorage(Drop drop);
	ProducerFactory addMapper(Mapper mapper);
	ProducerFactory addFileQuequeStorage(FileStorageReadOnly fileStorage);
	ProducerFactory addXmlParser(XmlParser parser);
	ProducerFactory addFileProvider(FileProvider fileProvider);
	Producer createProducer();
	
}
