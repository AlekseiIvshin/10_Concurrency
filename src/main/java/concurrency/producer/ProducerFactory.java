package concurrency.producer;

import mapper.Mapper;
import xml.provider.XmlProviderFactory;

import common.exception.FactoryException;
import common.fileprovider.FileProviderFactory;

import concurrency.queuestorages.drop.DropSetter;
import concurrency.queuestorages.files.FileGetter;

/**
 * Factory create new Producer instance which setted parameters.
 * 
 * @author Aleksei_Ivshin
 *
 */
public interface ProducerFactory {

	/**
	 * Set drop queue for Producer.
	 * 
	 * @param drop
	 *            queue
	 * @return this factory instance.
	 */
	ProducerFactory setDropStorage(DropSetter drop);

	/**
	 * Set mapper for Producer.
	 * 
	 * @param mapper
	 * @return this factory instance.
	 */
	ProducerFactory setMapper(Mapper mapper);

	/**
	 * Set file queue for Producer.
	 * 
	 * @param fileStorage
	 * @return this factory instance.
	 */
	ProducerFactory setFileQueueStorage(FileGetter fileStorage);

	/**
	 * 
	 * @param parser
	 * @return this factory instance.
	 */
	ProducerFactory setXmlProviderFactory(XmlProviderFactory parser);

	/**
	 * Set file provider for Producer.
	 * 
	 * @param fileProvider
	 * @return this factory instance.
	 */
	ProducerFactory setFileProviderFactory(FileProviderFactory fileProviderFactory);

	/**
	 * Create new producer instance.
	 * 
	 * @return new producer
	 * @throws FactoryException
	 */
	Producer createProducer() throws FactoryException;

}
