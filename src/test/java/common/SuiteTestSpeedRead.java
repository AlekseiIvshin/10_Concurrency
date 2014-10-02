package common;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import xml.provider.JAXBProviderTest;
import xml.provider.JAXBplusStAXProviderTest;

@RunWith(Categories.class)
@IncludeCategory(SpeedTest.class)
@SuiteClasses(value = { JAXBProviderTest.class, JAXBplusStAXProviderTest.class })
public class SuiteTestSpeedRead {

}
