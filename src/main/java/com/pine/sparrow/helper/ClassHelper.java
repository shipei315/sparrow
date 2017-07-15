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
		return getAnnotationClass(Service.class);
	}
	
	public static Set<Class<?>> getControllerClassSet(){
		return getAnnotationClass(Controller.class);
	}
	
	public static Set<Class<?>> getBeanClassSet(){
		Set<Class<?>> beanClassSet = new HashSet<Class<?>>();
		beanClassSet.addAll(getControllerClassSet());
		beanClassSet.addAll(getServiceClassSet());
		return beanClassSet;
	}
	
	private static Set<Class<?>> getAnnotationClass(Class<? extends Annotation> cls){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> tempCls : CLASS_SET){
			if(tempCls.isAnnotationPresent(cls)){
				classSet.add(tempCls);
			}
		}
		return classSet;
	}
}
