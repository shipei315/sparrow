package com.pine.sparrow.classutil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pine.sparrow.exception.SparrowException;

/**
 * 反射工具类
 * @author shipei.sp
 *
 */
public class ReflectionUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);
	
	/**
	 * 反射产生具体的对象
	 * @param cls
	 * @return
	 */
	public static Object newInstance(Class<?> cls){
		Object instance;
		try{
			instance = cls.newInstance();
		}catch (Exception e) {
			logger.error("fail to generate instance", e);
			throw new SparrowException("fail to generate instance", e);
		}
		return instance;
	}
	
	/**
	 * 调用具体的方法
	 * @param obj
	 * @param method
	 * @param args
	 * @return
	 */
	public static Object invokeMethod(Object obj, Method method, Object ...args ){
		Object res;
		try{
			method.setAccessible(true);
			res = method.invoke(obj, args);
		}catch (Exception e) {
			logger.error("fail to generate instance", e);
			throw new SparrowException("fail to generate instance", e);
		}
		return res;
	}
	
	/**
	 * 设置成员变量的值
	 * @param obj
	 * @param field
	 * @param value
	 */
	public static void setField(Object obj, Field field, Object value){
		try{
			field.setAccessible(true);
			field.set(obj, value);
		}catch (Exception e) {
			logger.error("fail to set field instance", e);
			throw new SparrowException("fail to set field instance", e);
		}
	}
}
