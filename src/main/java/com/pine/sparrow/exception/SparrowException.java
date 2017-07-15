package com.pine.sparrow.exception;

/**
 * 自定义异常类
 * @author shipei.sp
 *
 */
@SuppressWarnings("serial")
public class SparrowException extends RuntimeException {

	public SparrowException(String msg) {
		super(msg);
	}
	
	public SparrowException(String msg, Exception e) {
		super(msg, e);
	}

}
