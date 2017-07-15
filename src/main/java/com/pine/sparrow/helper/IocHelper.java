package com.pine.sparrow.helper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

import com.pine.sparrow.annotation.Autowired;
import com.pine.sparrow.classutil.ReflectionUtil;

public class IocHelper {
	
	static {
		// 拿到所有的待设置属性的bean
		Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
		
		if(CollectionUtils.isNotEmpty(beanMap.keySet())){
			for(Entry<Class<?>, Object> entry : beanMap.entrySet()){
				Class<?> cls = entry.getKey();
				Object beanInstance = entry.getValue();
				// 取出所有的属性，注入具体的值
				Field [] fields = cls.getDeclaredFields();
				
				if(!Objects.isNull(fields) && fields.length > 0){
					for(Field field : fields){
						if (field.isAnnotationPresent(Autowired.class)){
							Class<?> filedCls = field.getType();
							Object fieldValue = beanMap.get(filedCls);
							
							if(!Objects.isNull(fieldValue)){
								ReflectionUtil.setField(beanInstance, field, fieldValue);
							}
						}
					}
				}
			}
		}
	}

}
