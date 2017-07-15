package com.pine.sparrow.helper;

import com.pine.sparrow.classutil.ClassUtil;

/**
 * 
 * @author shipei.sp
 *
 */
public class LoaderHelper {
	
	public static void init(){
		
		Class<?> [] classList = {ClassHelper.class,BeanHelper.class,IocHelper.class, ControllerHelper.class};
		
		for(Class<?> cls : classList){
			ClassUtil.loadClass(cls.getName(), false);
		}
	}
}
