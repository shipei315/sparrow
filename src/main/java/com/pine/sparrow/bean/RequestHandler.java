package com.pine.sparrow.bean;

import java.lang.reflect.Method;

/**
 * 内置Bean
 * <br/>处理请求对象的具体对象
 * @author shipei.sp
 *
 */
public class RequestHandler {
	
	private Class<?> controllerClass;
	
	private Method actionMethod;
	
	public RequestHandler(Class<?> controllerClass, Method actionMethod){
		this.controllerClass = controllerClass;
		this.actionMethod = actionMethod;
	}

	public Class<?> getControllerClass() {
		return controllerClass;
	}

	public void setControllerClass(Class<?> controllerClass) {
		this.controllerClass = controllerClass;
	}

	public Method getActionMethod() {
		return actionMethod;
	}

	public void setActionMethod(Method actionMethod) {
		this.actionMethod = actionMethod;
	}

}
