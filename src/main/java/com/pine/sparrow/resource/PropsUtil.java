package com.pine.sparrow.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pine.sparrow.exception.SparrowException;

/**
 * 资源文件处理类
 * @author shipei.sp
 *
 */
public final class PropsUtil {

	private static final Logger logger = LoggerFactory.getLogger(PropsUtil.class);

	public static Properties loadProps(String fileName){
		Properties properties = null;
		InputStream inputStream = null;
		try{
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			properties = new Properties();
			properties.load(inputStream);
		}catch (Exception e) {
			logger.error("fail to load properties file!", e);
			throw new SparrowException("fail to load properties file", e);
		}finally {
			if (!Objects.isNull(inputStream)) {
				try{
					inputStream.close();
				}catch(IOException e){
					logger.error("fail to close inputStream", e);
					throw new SparrowException("fail to close inputStream", e);
				}
			}
		}
		return properties;
	}
	
	public static String getString(Properties properties, String key){
		return getString(properties, key, "");
	}
	
	public static String getString(Properties properties, String key, String defaultValue){
		String value = defaultValue;
		if(properties.containsKey(key)){
			value = properties.getProperty(key);
		}
		return value;
	}
	
	public static int getInt(Properties properties, String key){
		return getInt(properties, key, 0);
	}
	
	public static int getInt(Properties properties, String key, int defaultValue){
		int value = defaultValue;
		if(properties.containsKey(key)){
			value = Integer.valueOf(properties.getProperty(key));
		}
		return value;
	}
	
	public static boolean getBoolean(Properties properties, String key){
		return getBoolean(properties, key,false);
	}
	
	public static boolean getBoolean(Properties properties, String key, boolean defaultValue){
		boolean value = defaultValue;
		if(properties.containsKey(key)){
			value = Boolean.valueOf(properties.getProperty(key));
		}
		return value;
	}
}
