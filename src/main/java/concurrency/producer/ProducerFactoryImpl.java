package concurrency.producer;

import common.FactoryException;
import common.FileProvider;
import concurrency.quequestorages.drop.Drop;
import concurrency.quequestorages.drop.DropSetter;
import concurrency.quequestorages.files.FileStorageReadOnly;
import mapper.Mapper;
import xml.provider.XmlProvider;
import xml.provider.XmlProviderFactory;

public class ProducerFactoryImpl implements ProducerFactory {
	private DropSetter drop;
	private Mapper mapper;
	private XmlProviderFactory parser;
	private FileStorageReadOnly fileStorage;
	private FileProvider fileProvider;
	
	
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
	public ProducerFactory setFileQuequeStorage(FileStorageReadOnly fileStorage) {
		this.fileStorage = fileStorage;
		return this;
	}

	@Override
	public ProducerFactory setXmlProviderFactory(XmlProviderFactory parser) {
		this.parser = parser;
		return this;
	}
	
	@Override
	public ProducerFactory setFileProvider(FileProvider fileProvider) {
		this.fileProvider = fileProvider;
		return this;
	}

	@Override
	public Producer createProducer() throws FactoryException {
		try{
			return new ProducerImpl(drop, mapper, fileProvider, parser.createProvider(), fileStorage);
		} catch (NullPointerException e){
			throw new FactoryException("Some components are null or weren't setted to factory. "+e.getMessage());
		}
	}


}
