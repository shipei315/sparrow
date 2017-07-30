package com.pine.sparrow.helper;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import com.pine.sparrow.annotation.Controller;
import com.pine.sparrow.annotation.Service;
import com.pine.sparrow.classutil.ClassUtil;
import com.pine.sparrow.config.ConfigHelper;

public class ClassHelper {
	
	
	private static final Set<Class<?>> CLASS_SET;
	
	static {
		String basePackage = ConfigHelper.getAppBasePackage();
		CLASS_SET = ClassUtil.getClassSet(basePackage);
	}
	
	public static Set<Class<?>> getServiceClassSet(){
		return getClassSetByAnnotation(Service.class);
	}
	
	public static Set<Class<?>> getControllerClassSet(){
		return getClassSetByAnnotation(Controller.class);
	}
	
	/**
	 * 获取所有的bean class实例，包括被Controller注解和被Service注解的实例
	 * @return
	 */
	public static Set<Class<?>> getBeanClassSet(){
		Set<Class<?>> beanClassSet = new HashSet<Class<?>>();
		beanClassSet.addAll(getControllerClassSet());
		beanClassSet.addAll(getServiceClassSet());
		return beanClassSet;
	}
	
	public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> cls){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> tempCls : CLASS_SET){
			if(tempCls.isAnnotationPresent(cls)){
				classSet.add(tempCls);
			}
		}
		return classSet;
	}
	
	/**
	 * 获取应用包名下某父类（或者接口）的所有子类（或者实现类）
	 * @param superClass
	 * @return
	 */
	public static Set<Class<?>> getClassSetBySuperCls(Class<?> superClass){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> tempCls : CLASS_SET){
			if(tempCls.isAssignableFrom(superClass) && !superClass.equals(tempCls)){
				classSet.add(tempCls);
			}
		}
		return classSet;
	}
}
