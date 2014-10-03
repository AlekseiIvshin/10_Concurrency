package common;

import mapper.MapperImplTest;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import concurrency.consumer.ConsumerImplTest;
import concurrency.producer.ProducerFactoryImplTest;
import concurrency.producer.ProducerImplTest;
import concurrency.quequestorages.DropImplTest;
import concurrency.quequestorages.FileStorageImplTest;

@RunWith(Categories.class)
@ExcludeCategory(SlowTest.class)
@SuiteClasses({ ProducerFactoryImplTest.class, ProducerImplTest.class,
		DropImplTest.class, FileStorageImplTest.class,
		ConsumerImplTest.class, MapperImplTest.class })
public class SuiteTest {

}
