package com.utility;

import java.io.FileInputStream;
import java.util.Properties;

public class ReadFiles {
	
	private Properties properties;
	
	public ReadFiles() {
        properties = new Properties();
        String path = "./src/main/resources/static/requiredResources/data.properties";
        try (FileInputStream fis = new FileInputStream(path)) {
            
            properties.load(fis);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	public String getValues(String key) {
		String value = null;
		
		value = properties.getProperty(key);
		
		
		return value;
	}

}
