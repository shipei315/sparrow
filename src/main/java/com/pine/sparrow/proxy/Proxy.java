package com.pine.sparrow.proxy;

public interface Proxy {
	
	Object doProxy(ProxyChain proxyChain) throws Throwable;

}
