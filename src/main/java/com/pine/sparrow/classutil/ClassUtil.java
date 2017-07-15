package com.pine.sparrow.classutil;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pine.sparrow.exception.SparrowException;

/**
 * 类加载工具类
 * 
 * @author shipei.sp
 *
 */
public final class ClassUtil {

	private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 载入指定的类名
	 * @param className
	 * @param initialize
	 * @return
	 */
	public static Class<?> loadClass(String className, boolean initialize) {
		Class<?> cls;
		try {
			cls = Class.forName(className, initialize, getClassLoader());
		} catch (ClassNotFoundException e) {
			logger.error("fail to load class", e);
			throw new SparrowException("fail to load class", e);
		}
		return cls;
	}

	/**
	 * 加载某个包下的所有类
	 * <br/>包含文件中的类和jar包中的类
	 * @param packageName
	 * @return
	 */
	public static Set<Class<?>> getClassSet(String packageName) {
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		try {
			Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (!Objects.isNull(url)) {
					String protocol = url.getProtocol();
					if ("file".equals(protocol)) {
						String packagePath = url.getPath().replaceAll("20%", " ");
						addClass(classSet, packagePath, packageName);
					} else if ("jar".equals(protocol)) {
						JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
						if(!Objects.isNull(jarURLConnection)){
							JarFile jarFile = jarURLConnection.getJarFile();
							if(!Objects.isNull(jarFile)){
								Enumeration<JarEntry> jarEntries = jarFile.entries();
								while(jarEntries.hasMoreElements()){
									JarEntry jarEntry = jarEntries.nextElement();
									String jarEntryName = jarEntry.getName();
									if(StringUtils.endsWith(jarEntryName, ".class")){
										String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
										doAddClass(classSet, className);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return classSet;
	}

	private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {

		File[] files = new File(packagePath).listFiles(new FileFilter() {
			public boolean accept(File file) {
				// TODO Auto-generated method stub
				return file.isDirectory() || (file.isFile() && file.getName().endsWith(".class"));
			}
		});

		for (File file : files) {
			String fileName = file.getName();
			if (file.isFile()) {
				String className = fileName.substring(0, fileName.lastIndexOf("."));
				if (!StringUtils.isBlank(packageName)) {
					className = packageName + className;
					doAddClass(classSet, className);
				}
			} else {
				String subPackagePath = fileName ;
				if(StringUtils.isNotEmpty(packagePath)){
					subPackagePath = packagePath + "/" + subPackagePath;
				}
				
				String subPackageName = fileName ;
				if(StringUtils.isNotEmpty(packageName)){
					subPackagePath = packageName + "." + subPackageName;
				}
				
				addClass(classSet, packagePath, packageName);
			}
		}
	}

	private static void doAddClass(Set<Class<?>> classSet, String className) {
		Class<?> cls = loadClass(className, false);
		classSet.add(cls);
	}
}
