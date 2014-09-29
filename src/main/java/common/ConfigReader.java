package common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
	public final static String fileName = "config.properties";

	public String getValue(String propertyName) throws IOException {
		Properties prop = new Properties();
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(fileName);
		prop.load(inputStream);
		if (inputStream == null) {
			throw new FileNotFoundException("property file '" + fileName
					+ "' not found in the classpath");
		}

		return prop.getProperty(propertyName);

	}
}
