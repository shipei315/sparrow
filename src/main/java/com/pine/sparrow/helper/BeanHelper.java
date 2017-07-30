package com.pine.sparrow.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.pine.sparrow.classutil.ReflectionUtil;

/**
 * 接收class对象，转化为具体的object
 * 同时使用map存储结果
 * @author shipei.sp
 *
 */
public class BeanHelper {
	
	private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();
	
	static {
		Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
		for(Class<?> cls : beanClassSet){
			Object obj = ReflectionUtil.newInstance(cls);
			BEAN_MAP.put(cls, obj);
		}
	}
	
	/**
	 * 返回class 对象和该class对应的具体的bean对象
	 * @return
	 */
	public static Map<Class<?>, Object> getBeanMap(){
		return BEAN_MAP;
	}
	
	public static <T> T getBean(Class<T> cls){
		return (T) BEAN_MAP.get(cls);
	}

	/**
	 * 设置bean 实例
	 * @param cls
	 * @param obj
	 * @return
	 */
	public static <T> T setBean(Class<T> cls, Object obj){
		return (T) BEAN_MAP.put(cls, obj);
	}
}
