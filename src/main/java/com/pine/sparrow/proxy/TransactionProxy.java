package com.pine.sparrow.proxy;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pine.sparrow.annotation.Transactional;
import com.pine.sparrow.helper.DataBaseHelper;

public class TransactionProxy implements Proxy{
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionProxy.class);
	
	private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>(){
		
		@Override
		protected Boolean initialValue() {
			return false;
		}
		
	};

	public Object doProxy(ProxyChain proxyChain) throws Throwable {
		Object result;
		boolean flag = FLAG_HOLDER.get();
		Method method = proxyChain.getTargetMethod();
		
		if(!flag && method.isAnnotationPresent(Transactional.class)){
			FLAG_HOLDER.set(true);
			
			try{
				DataBaseHelper.beginTransaction();
				result = proxyChain.doProxyChain();
				DataBaseHelper.commitTransaction();
			}catch(Exception e){
				DataBaseHelper.rollbackTransaction();
				logger.error("rollback transaction");
				throw e;
			}finally {
				FLAG_HOLDER.remove();
			}
			
		}else{
			result = proxyChain.doProxyChain();
		}
		return result;
	}
	
	
}
