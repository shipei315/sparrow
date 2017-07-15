package com.pine.sparrow.config;

import java.util.Properties;

import com.pine.sparrow.resource.PropsUtil;

/**
 * 配置加载类
 * @author shipei.sp
 *
 */
public final class ConfigHelper {
	
	private static final Properties CONFIG_PORPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);
	
	public static String getJdbcDriver(){
		return PropsUtil.getString(CONFIG_PORPS, ConfigConstant.JDBC_DRIVER);
	}
	
	public static String getJdbcUrl(){
		return PropsUtil.getString(CONFIG_PORPS, ConfigConstant.JDBC_URL);
	}
	
	public static String getJdbcUserName(){
		return PropsUtil.getString(CONFIG_PORPS, ConfigConstant.JDBC_USERNAME);
	}
	
	public static String getJdbcPassword(){
		return PropsUtil.getString(CONFIG_PORPS, ConfigConstant.JDBC_PASSWORD);
	}
	
	public static String getAppBasePackage(){
		return PropsUtil.getString(CONFIG_PORPS, ConfigConstant.APP_BASE_PACKAGE);
	}
	
	public static String getAppJspPath(){
		return PropsUtil.getString(CONFIG_PORPS, ConfigConstant.APP_JSP_PATH, ConfigConstant.DEFAULT_APP_JSP_PATH);
	}
	
	public static String getAppAssetPath(){
		return PropsUtil.getString(CONFIG_PORPS, ConfigConstant.APP_JSP_PATH, ConfigConstant.DEFAULT_APP_ASSET_PATH);
	}
}
