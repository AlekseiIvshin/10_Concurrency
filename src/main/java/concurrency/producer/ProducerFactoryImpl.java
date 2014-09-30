package concurrency.producer;

import common.FileProvider;
import concurrency.quequestorages.Drop;
import concurrency.quequestorages.FileStorageReadOnly;
import mapper.Mapper;
import xml.provider.XmlProvider;

public class ProducerFactoryImpl implements ProducerFactory {
	private Drop drop;
	private Mapper mapper;
	private XmlProvider parser;
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
	public ProducerFactory addXmlProvider(XmlProvider parser) {
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
