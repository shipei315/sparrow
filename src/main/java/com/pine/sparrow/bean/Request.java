package com.pine.sparrow.bean;

/**
 * 内置Bean
 * <br/>包装传过来的请求对象
 * @author shipei.sp
 *
 */
public class Request {
	
	private String requestMethod;
	
	private String requestPath;
	
	public Request(String requestMethod, String requestPath){
		this.requestMethod = requestMethod;
		this.requestPath = requestPath;
	}
	
	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}


}
