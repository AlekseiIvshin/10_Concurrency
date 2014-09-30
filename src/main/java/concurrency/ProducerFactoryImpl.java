package concurrency;

import common.FileProvider;

import mapper.Mapper;
import xml.XmlParser;

public class ProducerFactoryImpl implements ProducerFactory {
	private Drop drop;
	private Mapper mapper;
	private XmlParser parser;
	private FileStorageReadOnly fileStorage;
	private FileProvider fileProvider;
	
	
	@Override
	public ProducerFactory addDropStorage(Drop drop) {
		this.drop = drop;
		return this;
	}

	@Override
	public ProducerFactory addMapper(Mapper mapper) {
		this.mapper = mapper;
		return this;
	}

	@Override
	public ProducerFactory addFileQuequeStorage(FileStorageReadOnly fileStorage) {
		this.fileStorage = fileStorage;
		return this;
	}

	@Override
	public ProducerFactory addXmlParser(XmlParser parser) {
		this.parser = parser;
		return this;
	}
	
	@Override
	public ProducerFactory addFileProvider(FileProvider fileProvider) {
		this.fileProvider = fileProvider;
		return this;
	}

	@Override
	public Producer createProducer() {
		return new ProducerImpl(drop, mapper, fileProvider, parser, fileStorage);
	}


}
