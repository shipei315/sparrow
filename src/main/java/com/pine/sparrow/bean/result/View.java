package com.pine.sparrow.bean.result;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装返回的视图模型，包括返回的路径和返回的数据模型
 * @author shipei.sp
 *
 */
public class View {
	
	private String path;
	
	private Map<String, Object> model;
	
	public View(String path, Map<String, Object> model){
		this.path = path;
		this.model = new HashMap<String, Object>();
	}
	
	public View addModel(String key, String value){
		model.put(key, value);
		return this;
	}
	
	public String getPath(){
		return path;
	}
	
	public Map<String, Object> getModel(){
		return model;
	}
}
