package com.pine.sparrow.proxy;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerAspect extends AspectProxy{
	
	private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);
	
	private long begin;
	
	public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
		logger.debug("-------- begin --------, class is {}, method is {}", cls, method.getName());
		begin = System.currentTimeMillis();
	}
	
	public void after(Class<?> cls, Method method, Object[] params) throws Throwable {
		logger.debug("-------- end --------, cost {}", (System.currentTimeMillis() - begin));
	}
}
