package com.pine.sparrow.helper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.pine.sparrow.annotation.Action;
import com.pine.sparrow.bean.Request;
import com.pine.sparrow.bean.RequestHandler;

/**
 * 对Controller注解的类进行处理
 * <br>主要将标注为Controller类中标注为Action注解的方法处理成请求和处理对象的映射
 * @author shipei.sp
 *
 */
public class ControllerHelper {
	
	private static final Map<Request, RequestHandler> ACTION_MAP = new HashMap<Request, RequestHandler>();

	static {
		Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
		if(CollectionUtils.isNotEmpty(controllerClassSet)){
			for(Class<?> cls : controllerClassSet){
				Method [] methods = cls.getMethods();
				
				if(!Objects.isNull(methods)){
					for(Method method : methods){
						if(method.isAnnotationPresent(Action.class)){
							Action action = method.getAnnotation(Action.class);
							String mapping = action.value();
							if(mapping.matches("\\w+:\\w*")){
								String [] array = mapping.split(":");
								if(!Objects.isNull(array) && array.length == 2){
									String requestMethod = array[0];
									String requestPath = array[1];
									Request request = new Request(requestMethod, requestPath);
									RequestHandler requestHandler = new RequestHandler(cls, method);
									ACTION_MAP.put(request, requestHandler);
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param requestMethod
	 * @param requestPath
	 * @return
	 */
	public static RequestHandler getRequestHandler(String requestMethod, String requestPath){
		Request request = new Request(requestMethod, requestPath);
		return ACTION_MAP.get(request);
	}
}
