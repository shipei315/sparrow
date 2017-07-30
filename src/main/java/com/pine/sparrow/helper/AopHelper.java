package com.pine.sparrow.helper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pine.sparrow.annotation.Aspect;
import com.pine.sparrow.proxy.AspectProxy;
import com.pine.sparrow.proxy.Proxy;
import com.pine.sparrow.proxy.ProxyManager;
import com.pine.sparrow.proxy.TransactionProxy;

public class AopHelper {

	private static Logger logger = LoggerFactory.getLogger(AopHelper.class);

	static {
		try {

			Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
			Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);

			for (Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
				Class<?> targetClass = targetEntry.getKey();
				List<Proxy> proxyList = targetEntry.getValue();

				Object proxy = ProxyManager.createProxy(targetClass, proxyList);

				BeanHelper.setBean(targetClass, proxy);
			}

		} catch (Exception e) {
			logger.error("aop failure", e);
		}
	}

	private static Set<Class<?>> createTargetClassSet(Aspect aspect) {

		Set<Class<?>> targetClassSet = new HashSet<Class<?>>();

		Class<? extends Annotation> annotation = aspect.value();
		if (annotation != null && !annotation.equals(Aspect.class)) {
			targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
		}

		return targetClassSet;
	}

	/**
	 * 找出切面逻辑和具体实现类的对应关系
	 * 
	 * @return
	 */
	private static Map<Class<?>, Set<Class<?>>> createProxyMap() {
		Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();
		// AspectProxy
		addAspectProxy(proxyMap);
		// 事物注解
		addTransactionProxy(proxyMap);
		return proxyMap;
	}
	
	private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap){
		Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuperCls(AspectProxy.class);
		for (Class<?> proxyClass : proxyClassSet) {

			if (proxyClass.isAnnotationPresent(Aspect.class)) {
				// 找出横切功能作用的类，目前粒度较粗，作用于依据某个注解的类全部选中
				// 是否代理其中的方法依据切面类中的 intercept 方法判断
				Aspect aspect = proxyClass.getAnnotation(Aspect.class);
				Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
				proxyMap.put(proxyClass, targetClassSet);
			}

		}
	}
	
	private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap){
		Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuperCls(AspectProxy.class);
		proxyMap.put(TransactionProxy.class, proxyClassSet);
	}

	/**
	 * 代理类和实体对象的映射关系
	 * 
	 * @param proxyEntryMap
	 * @return
	 * @throws Exception
	 */
	private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyEntryMap)
			throws Exception {
		Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();

		for (Entry<Class<?>, Set<Class<?>>> entry : proxyEntryMap.entrySet()) {
			Class<?> proxyClass = entry.getKey();
			Set<Class<?>> targetClassSet = entry.getValue();

			for (Class<?> targetClass : targetClassSet) {

				Proxy proxy = (Proxy) proxyClass.newInstance();

				if (targetMap.containsKey(targetClass)) {
					targetMap.get(targetClass).add(proxy);
				} else {
					List<Proxy> proxyList = new ArrayList<Proxy>();
					proxyList.add(proxy);
					targetMap.put(targetClass, proxyList);
				}
			}
		}

		return targetMap;
	}
}
