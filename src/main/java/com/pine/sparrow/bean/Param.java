package com.pine.sparrow.bean;

import java.util.Map;

/**
 * 封装方法调用的参数
 * @author shipei.sp
 *
 */
public class Param {
	
	private Map<String, Object> paramMap;
	
	public Param(Map<String, Object> paramMap){
		this.paramMap = paramMap;
	}
	
	public long getLong(String name){
		return (Long)paramMap.get(name);
	}
	
	public Map<String, Object> getMap(){
		return paramMap;
	}
}
