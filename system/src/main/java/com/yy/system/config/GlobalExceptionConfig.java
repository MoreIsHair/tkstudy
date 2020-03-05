package com.yy.system.config;

import com.alibaba.fastjson.JSONObject;
import com.yy.common.bean.Result;
import com.yy.common.constant.ResponseCode;
import com.yy.common.exception.BizException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class GlobalExceptionConfig implements HandlerExceptionResolver {

	private final static Logger log = LoggerFactory.getLogger(GlobalExceptionConfig.class);

	private static final String DUBBO_RPC_EXCEPTION = "com.alibaba.dubbo.rpc.RpcException";

	/** 处理内部异常, 也就是未被其他异常方法处理的异常 */
	private Result<String> handleExceptionInternal(Exception ex ) {
		return processResponseData( ResponseCode.INTERNAL_EXCEPTION.getCode(), ResponseCode.INTERNAL_EXCEPTION.getMsg(),ex );
	}

	private Result<String> handleMissingServletRequestPart(
			MissingServletRequestPartException ex ) {
		return processResponseData( ResponseCode.MISSING_PATH_VARIABL_EEXCEPTION.getCode(), ResponseCode.MISSING_PATH_VARIABL_EEXCEPTION.getMsg() );
	}

	private Result<String> handleBindException( BindException ex ) {
		return processResponseData( ResponseCode.BIND_EXCEPTION.getCode(),  ResponseCode.BIND_EXCEPTION.getMsg() );
	}

	private Result<String> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex ) {
		return processResponseData( ResponseCode.METHOD_ARGUMENT_NOTVALID_EXCEPTION.getCode(), ResponseCode.METHOD_ARGUMENT_NOTVALID_EXCEPTION.getMsg());
	}

	private Result<String> handleHttpMessageNotWritable(
			HttpMessageNotWritableException ex ) {
		return processResponseData( ResponseCode.HTTP_MESSAGE_NOTWRITABLE_EXCEPTION.getCode(), ResponseCode.HTTP_MESSAGE_NOTWRITABLE_EXCEPTION.getMsg());
	}

	private Result<String> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex ) {
		return processResponseData( ResponseCode.HTTP_MESSAGE_NOTREADABLE_EXCEPTION.getCode(), ResponseCode.HTTP_MESSAGE_NOTREADABLE_EXCEPTION.getMsg() );
	}

	/**
	 * 参数类型不匹配
	 */
	private Result<String> handleTypeMismatch( TypeMismatchException ex ) {
		return processResponseData( ResponseCode.TYPEM_ISMATCH_EXCEPTION.getCode(), ResponseCode.TYPEM_ISMATCH_EXCEPTION.getMsg() );
	}

	/**
	 * 参数转换失败
	 */
	private Result<String> handleConversionNotSupported(
			ConversionNotSupportedException ex ) {
		return processResponseData( ResponseCode.CONVERSION_NOTSUPPORTED_EXCEPTION.getCode(), ResponseCode.CONVERSION_NOTSUPPORTED_EXCEPTION.getMsg() );
	}

	/**
	 * 参数绑定失败
	 */
	private Result<String> handleServletRequestBindingException(
			ServletRequestBindingException ex ) {
		return processResponseData( ResponseCode.SERVLETREQUEST_BINDING_EXCEPTION.getCode(), ResponseCode.SERVLETREQUEST_BINDING_EXCEPTION.getMsg() );
	}

	/**
	 * 缺少请求参数
	 */
	private Result<String> handleMissingServletRequestParameter(
			MissingServletRequestParameterException ex ) {
		return processResponseData( ResponseCode.MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION.getCode(), ResponseCode.MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION.getMsg() );
	}

	/**
	 * 响应的类型不匹配
	 */
	private Result<String> handleHttpMediaTypeNotAcceptable(
			HttpMediaTypeNotAcceptableException ex ) {
		return processResponseData( ResponseCode.HTTP_MEDIATYPE_NOTACCEPTABLE_EXCEPTION.getCode(), ResponseCode.HTTP_MEDIATYPE_NOTACCEPTABLE_EXCEPTION.getMsg() );
	}

	/**
	 * 请求的类型不存在
	 */
	private Result<String> handleHttpMediaTypeNotSupported(
			HttpMediaTypeNotSupportedException ex ) {
		return processResponseData( ResponseCode.HTTP_MEDIATYPE_NOTSUPPORTED_EXCEPTION.getCode(),  ResponseCode.HTTP_MEDIATYPE_NOTSUPPORTED_EXCEPTION.getMsg());
	}

	/**
	 * 请求的方法不支持
	 */
	private Result<String> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex ) {
		return processResponseData( ResponseCode.HTTPREQUEST_METHOD_NOTSUPPORTED_EXCEPTION.getCode(),  ResponseCode.HTTPREQUEST_METHOD_NOTSUPPORTED_EXCEPTION.getMsg());
	}

	/**
	 * 处理自定义异常
	 */
	private Result<String> handlBizException( BizException ex ) {
		Integer errorCodes = ResponseCode.INTERNAL_EXCEPTION.getCode();
		if(ex.getErrorCode() != null && ex.getErrorCode().intValue()>0){
			errorCodes = ex.getErrorCode();
		}
		return processResponseData( errorCodes, ex.getMessage(),ex );
	}

	/**
	 * 处理DUBBO RPC异常
	 */
	private Result<String> handlDubboRpcException( Exception ex ) {
		return processResponseData( ResponseCode.INTERNAL_DUBBO_EXCEPTION.getCode(), ResponseCode.INTERNAL_DUBBO_EXCEPTION.getMsg() ,ex );
	}

	/**
	 * 处理SQL异常
	 */
	private Result<String> handlDubboRpcException( BadSqlGrammarException ex ) {
		return processResponseData( ResponseCode.INTERNAL_SQL_EXCEPTION.getCode(), ResponseCode.INTERNAL_SQL_EXCEPTION.getMsg() ,ex );
	}

	/**
	 * 处理Shiro无权限异常
	 */
	private Result<String> handlUnAuthorizedException(UnauthorizedException ex ) {
		return processResponseData( ResponseCode.SHIRO_UNAUTHORIZEDEXCEPTION_EXCEPTION.getCode(), ResponseCode.SHIRO_UNAUTHORIZEDEXCEPTION_EXCEPTION.getMsg() ,ex );
	}

	/**
	 * 处理Shiro未登录
	 */
	private Result<String> handlUnauthenticatedException(UnauthenticatedException ex ) {
		return processResponseData( ResponseCode.SHIRO_UNAUTHENTICATEDEXCEPTION_EXCEPTION.getCode(), ResponseCode.SHIRO_UNAUTHENTICATEDEXCEPTION_EXCEPTION.getMsg() ,ex );
	}


	/**
	 * 统一输出格式
	 * 
	 * @param errorCode - 
	 * @param message - 消息
	 * @return - ResponseData<String>
	 */
	private static Result<String> processResponseData( Integer errorCode, String message ) {
		Result<String> responseData = new Result<>();
		responseData.setMsg( message == null ? "" : message );
		responseData.setCode( errorCode );
		return responseData;
	}
	
	/**
	 * 统一输出格式
	 * 
	 * @param errorCode -
	 * @param message - 消息
	 * @return - ResponseData<String>
	 */
	private Result<String> processResponseData( Integer errorCode, String message ,Throwable t) {
		Result<String> responseData = new Result<>();
		responseData.setMsg( message == null ? "" : message );
		responseData.setCode( errorCode );
		log.error(message,t);
		return responseData;
	}

	/*
	 * 重写异常处理, 覆盖spring的所有异常处理
	 */
	@Override
	public ModelAndView resolveException( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex ) {
		Result<String> responseData = null; 
		if( ex instanceof HttpRequestMethodNotSupportedException ) {
			responseData = handleHttpRequestMethodNotSupported( ( HttpRequestMethodNotSupportedException )ex ); 
		} else if( ex instanceof HttpMediaTypeNotSupportedException ) {
			responseData = handleHttpMediaTypeNotSupported( ( HttpMediaTypeNotSupportedException )ex );
		} else if( ex instanceof HttpMediaTypeNotAcceptableException ) {
			responseData = handleHttpMediaTypeNotAcceptable( ( HttpMediaTypeNotAcceptableException )ex );
		} else if( ex instanceof MissingServletRequestParameterException ) {
			responseData = handleMissingServletRequestParameter( ( MissingServletRequestParameterException )ex );
		} else if( ex instanceof ServletRequestBindingException ) {
			responseData = handleServletRequestBindingException( ( ServletRequestBindingException )ex );
		} else if( ex instanceof ConversionNotSupportedException ) {
			responseData = handleConversionNotSupported( ( ConversionNotSupportedException )ex );
		} else if( ex instanceof TypeMismatchException ) {
			responseData = handleTypeMismatch( ( TypeMismatchException )ex );
		} else if( ex instanceof HttpMessageNotReadableException ) {
			responseData = handleHttpMessageNotReadable( ( HttpMessageNotReadableException )ex );
		} else if( ex instanceof HttpMessageNotWritableException ) {
			responseData = handleHttpMessageNotWritable( ( HttpMessageNotWritableException )ex );
		} else if( ex instanceof MethodArgumentNotValidException ) {
			responseData = handleMethodArgumentNotValid( ( MethodArgumentNotValidException )ex );
		} else if( ex instanceof MissingServletRequestPartException ) {
			responseData = handleMissingServletRequestPart( ( MissingServletRequestPartException )ex );
		} else if( ex instanceof BindException ) {
			responseData = handleBindException( ( BindException )ex );
		} else if( ex instanceof BizException) {
			responseData = handlBizException( ( BizException )ex );
		} else if( ex instanceof BadSqlGrammarException){
			responseData = handlDubboRpcException(ex);
		}else if (ex instanceof UnauthenticatedException){
			responseData = handlUnauthenticatedException((UnauthenticatedException)ex);
		} else if (ex instanceof UnauthorizedException){
			responseData = handlUnAuthorizedException((UnauthorizedException)ex);
		}else if(DUBBO_RPC_EXCEPTION.equals(ex.getClass().getName())){
			responseData = handlDubboRpcException(ex);
		}else{
			responseData = handleExceptionInternal( ex );
		}
		// 以JSON格式输出
		response.setContentType( MediaType.APPLICATION_JSON_VALUE );
		response.setCharacterEncoding("UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.write( JSONObject.toJSONString( responseData ) );
			out.flush();
		} catch( IOException e ) {
			// TODO 出现致命错误
			// log.error( "ExceptionHandlerController write message error", e );
		}
		return null;
	}



}
