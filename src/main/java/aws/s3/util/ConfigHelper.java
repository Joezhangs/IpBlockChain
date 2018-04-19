package aws.s3.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfigHelper {
	private ConfigHelper() {
	};

	public static Properties propertie = null;
	static {
		propertie = new Properties();
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("util.properties");
		// 解决中文乱码
		BufferedReader buff = new BufferedReader(new InputStreamReader(inputStream));
		try {
			propertie.load(buff);
			inputStream.close();
		} catch (IOException e) {
		}
	}

	public static String getValue(String key) {
		return propertie.getProperty(key);
	}
}
