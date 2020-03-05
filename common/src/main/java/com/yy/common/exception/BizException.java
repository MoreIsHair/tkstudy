package com.yy.common.exception;


import com.yy.common.constant.ResponseCode;

/**
 * <p>Project:			<p>
 * <p>Module:			<p>
 * <p>Description:		<p>
 *
 */
public class BizException extends RuntimeException {

	private static final long serialVersionUID = 6362782762086492531L;

	private Integer errorCode;

	private String message;

	private Throwable throwable;

	public BizException() {
		super();
	}

	public BizException( String message ) {
		super(message);
		this.errorCode = ResponseCode.FAIL.getCode();
		this.message = message;
	}

	public BizException( int errorCode, Throwable throwable ) {
		super();
		this.errorCode = errorCode;
		this.throwable = throwable;
	}

	public BizException( String message, Throwable throwable ) {
		super(message);
		this.message = message;
		this.throwable = throwable;
	}

	public BizException(int errorCode,String message){
		super(message);
		this.message = message;
		this.errorCode = errorCode;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode( Integer errorCode ) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage( String message ) {
		this.message = message;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable( Throwable throwable ) {
		this.throwable = throwable;
	}

}
