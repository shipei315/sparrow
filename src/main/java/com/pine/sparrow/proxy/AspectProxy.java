package com.pine.sparrow.proxy;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AspectProxy implements Proxy {

	private static final Logger logger = LoggerFactory.getLogger(AspectProxy.class);

	public Object doProxy(ProxyChain proxyChain) throws Throwable {
		Object result = null;

		Class<?> cls = proxyChain.getTargetClass();
		Method method = proxyChain.getTargetMethod();
		Object[] params = proxyChain.getMethodParams();

		begin();

		try {
			if(intercept(cls, method, params)){
				before(cls, method, params);
				result = proxyChain.doProxyChain();
				after(cls, method, params);
			}else{
				result = proxyChain.doProxyChain();
			}
		} catch (Exception e) {
			logger.error("proxy failure, class is {}", cls, e);
			error(cls, method, params, e);
			throw e;
		} finally {
			end();
		}
		
		return result;
	}

	/**
	 * 钩子接口 开始方法
	 */
	public void begin() {

	}

	/**
	 * 是否拦截
	 * 
	 * @param cls
	 * @param method
	 * @param params
	 * @return
	 * @throws Throwable
	 */
	public boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable {
		return true;
	}

	/**
	 * 拦截方法之前调用钩子方法
	 * 
	 * @param cls
	 * @param method
	 * @param params
	 * @throws Throwable
	 */
	public void before(Class<?> cls, Method method, Object[] params) throws Throwable {

	}

	/**
	 * 拦截方法之后调用钩子方法
	 * 
	 * @param cls
	 * @param method
	 * @param params
	 * @throws Throwable
	 */
	public void after(Class<?> cls, Method method, Object[] params) throws Throwable {

	}

	/**
	 * 出错调用方法
	 * 
	 * @param cls
	 * @param method
	 * @param params
	 * @param e
	 */
	public void error(Class<?> cls, Method method, Object[] params, Throwable e) {

	}

	/**
	 * 结束方法
	 */
	public void end() {

	}
}
