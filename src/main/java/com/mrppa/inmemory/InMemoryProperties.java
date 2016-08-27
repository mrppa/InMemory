package com.mrppa.inmemory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class InMemoryProperties {
	private static Logger log = Logger.getLogger(InMemoryProperties.class.getName());
	private static InMemoryProperties inMemoryProperties = null;
	protected static String PROP_FILE_NAME="InMemory.properties";

	public static InMemoryProperties getInstance() {
		if (inMemoryProperties == null) {
			synchronized (InMemoryProperties.class) {
				inMemoryProperties = new InMemoryProperties();
				inMemoryProperties.prop = new Properties();
				
				InputStream inputStream = inMemoryProperties.getClass().getClassLoader().getResourceAsStream(PROP_FILE_NAME);

				try {
					inMemoryProperties.prop.load(inputStream);
				} catch (IOException e) {
					log.fatal("error while loading prop file", e);
					e.printStackTrace();
				}
			}
		}
		return inMemoryProperties;
	}

	private Properties prop;;

	private InMemoryProperties() {
	}

	public int getIntPropertyValue(String propKey) {
		String strValue = prop.getProperty(propKey);
		int value = Integer.parseInt(strValue.trim());
		log.debug(new StringBuffer(propKey).append("\t").append(value));
		return value;
	}

	public String getPropertyValue(String propKey) {
		String value = prop.getProperty(propKey);
		log.debug(new StringBuffer(propKey).append("\t").append(value));
		return value;
	}

	public List<String[]> getPropList(String prefix) {
		List<String[]> listProp = new ArrayList<String[]>();
		Iterator<Object> keyIt = prop.keySet().iterator();
		while (keyIt.hasNext()) {
			String key = (String) keyIt.next();
			if (key.startsWith(prefix)) {
				String[] propArr = new String[2];
				propArr[0] = key;
				propArr[1] = this.getPropertyValue(key);
				listProp.add(propArr);
			}
		}

		return listProp;

	}
}
