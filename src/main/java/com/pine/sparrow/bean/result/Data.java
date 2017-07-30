package com.pine.sparrow.bean.result;

/**
 * 返回的数据对象
 * <br/>该对象将被直接写入到HttpServletResponse对象中
 * @author shipei.sp
 *
 */
public class Data {
	
	private Object model;
	
	public Data(Object model){
		this.model = model;
	}
	
	public Object getModel(){
		return this.model;
	}

}
