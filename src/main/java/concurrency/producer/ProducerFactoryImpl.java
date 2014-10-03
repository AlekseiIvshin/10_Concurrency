package concurrency.producer;

import mapper.Mapper;
import xml.provider.XmlProviderFactory;

import common.exception.FactoryException;
import common.fileprovider.FileProviderFactory;

import concurrency.queuestorages.drop.DropSetter;
import concurrency.queuestorages.files.FileGetter;

public class ProducerFactoryImpl implements ProducerFactory {
	private DropSetter drop;
	private Mapper mapper;
	private XmlProviderFactory parser;
	private FileGetter fileStorage;
	private FileProviderFactory fileProviderFactory;

	@Override
	public ProducerFactory setDropStorage(DropSetter drop) {
		this.drop = drop;
		return this;
	}

	@Override
	public ProducerFactory setMapper(Mapper mapper) {
		this.mapper = mapper;
		return this;
	}

	@Override
	public ProducerFactory setFileQueueStorage(FileGetter fileStorage) {
		this.fileStorage = fileStorage;
		return this;
	}

	@Override
	public ProducerFactory setXmlProviderFactory(XmlProviderFactory parser) {
		this.parser = parser;
		return this;
	}

	@Override
	public ProducerFactory setFileProviderFactory(
			FileProviderFactory fileProviderFactory) {
		this.fileProviderFactory = fileProviderFactory;
		return this;
	}

	@Override
	public Producer createProducer() throws FactoryException {
		try {
			return new ProducerImpl(drop, mapper,
					fileProviderFactory.createProvider(),
					parser.createProvider(), fileStorage);
		} catch (NullPointerException e) {
			throw new FactoryException(
					"Some components are null or weren't setted to factory. "
							+ e.getMessage());
		}
	}

}
