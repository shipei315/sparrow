package com.pine.sparrow.helper;

import com.pine.sparrow.classutil.ClassUtil;

/**
 * 加载指定的工具类
 * <br/>执行其中的静态方法
 * @author shipei.sp
 *
 */
public class LoaderHelper {
	
	public static void init(){
		
		Class<?> [] classList = {ClassHelper.class, BeanHelper.class, AopHelper.class, IocHelper.class, ControllerHelper.class};
		
		for(Class<?> cls : classList){
			ClassUtil.loadClass(cls.getName(), false);
		}
	}
}
