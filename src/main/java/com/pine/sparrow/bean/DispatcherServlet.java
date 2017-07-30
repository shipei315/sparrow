package com.pine.sparrow.bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.pine.sparrow.bean.result.Data;
import com.pine.sparrow.bean.result.View;
import com.pine.sparrow.classutil.ReflectionUtil;
import com.pine.sparrow.config.ConfigHelper;
import com.pine.sparrow.exception.SparrowException;
import com.pine.sparrow.helper.BeanHelper;
import com.pine.sparrow.helper.ControllerHelper;
import com.pine.sparrow.helper.LoaderHelper;

@SuppressWarnings("serial")
public class DispatcherServlet extends HttpServlet {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void init(ServletConfig servletConfig) {
		// 初始化Helper相关类
		LoaderHelper.init();

		// 获取ServletContext对象，用于注册Servlet
		ServletContext servletContext = servletConfig.getServletContext();

		ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
		jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

		ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
		defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
	}

	/**
	 * 执行具体的Servlet方法
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestMethod = request.getMethod().toLowerCase();
		String requestPath = request.getPathInfo();
		// 依据请求的路径和请求的方法找到具体的处理类
		RequestHandler handler = ControllerHelper.getRequestHandler(requestMethod, requestPath);

		if (!Objects.isNull(handler)) {
			// 获取控制类
			Class<?> controllerClass = handler.getControllerClass();
			// 通过BeanHelper获取具体的对象
			Object object = BeanHelper.getBean(controllerClass);
			// 封装请求参数
			Map<String, Object> paramMap = getParamMap(request);
			Param  param = new Param(paramMap);
			Method method = handler.getActionMethod();
			// 反射得到具体的执行结果
			Object result = ReflectionUtil.invokeMethod(object, method, param);
			// 处理具体的执行结果
			hanbleResult(result, request, response);
		}
	}

	/**
	 * 拿出具体的请求参数
	 * <br>参数来源于两个地方，1是parammeters 2是从body中得到请求参数
	 * @param request
	 * @return
	 */
	private Map<String, Object> getParamMap(HttpServletRequest request){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Enumeration<String> paramNames = request.getParameterNames();
		
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String paramValue = request.getParameter(paramName);
			paramMap.put(paramName, paramValue);
		}
		
		try {
			String body = URLDecoder.decode(IOUtils.toString(request.getInputStream(),"utf-8"), "utf-8");
			if(StringUtils.isNotEmpty(body)) {
				String [] params = StringUtils.split(body, "&");
				if(ArrayUtils.isNotEmpty(params)){
					for(String param : params){
						String [] tempArray = StringUtils.split(param, "=");
						if(ArrayUtils.isNotEmpty(tempArray) && tempArray.length == 2){
							paramMap.put(tempArray[0], tempArray[1]);
						}
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("unsupported encode exception!", e);
			throw new SparrowException("unsupported encode exception!", e);
		} catch (IOException e) {
			logger.error("fail to get string from request inputStream!", e);
			throw new SparrowException("fail to get string from request inputStream!", e);
		}
		return paramMap;
	}
	
	private void hanbleResult(Object result, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		if(result instanceof View){
			View view = (View)result;
			String path = view.getPath();
			
			if(StringUtils.isNotBlank(path)){
				if(StringUtils.startsWith(path, "/")){
					response.sendRedirect(request.getContextPath() + path);
				} else {
					Map<String, Object> model = view.getModel();
					for(Entry<String, Object> entry : model.entrySet()){
						request.setAttribute(entry.getKey(), entry.getValue());
					}
					request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);;
				}
			}
		}else if (result instanceof Data){
			Data data = (Data) result;
			Object model = data.getModel();
			
			if(!Objects.isNull(model)){
				response.setContentType("application/json");
				response.setCharacterEncoding("utf-8");
				PrintWriter printWriter = response.getWriter();
				printWriter.write(JSONObject.toJSONString(model));
				printWriter.flush();
				printWriter.close();
			}
		}
	}

}
