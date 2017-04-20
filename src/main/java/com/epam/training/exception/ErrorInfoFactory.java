package com.epam.training.exception;

import com.epam.training.exception.ErrorInfo.ErrorType;
import com.epam.training.exception.ErrorInfo.Severity;

/**
 * ErrorInfoFactory to make frequently used ErrorItems 
 * 
 * @author Ferenc Kis
 * @version 1.1
 */
public class ErrorInfoFactory {
	/**
	 * Context initialization error 
	 * 
	 */	
	public static final ErrorInfo getInitializaionErrorInfo(
			String context, String errorDescription, String parameterName, Object parameterValue) {
		
		ErrorInfo info = new ErrorInfo();
		
		info.setErrorId("InitializaionError");
		info.setContextId(context);
		
		info.setErrorType(ErrorType.INTERNAL_ERROR);
		info.setSeverity(Severity.ERROR);
		
		info.setErrorDescription(errorDescription);
		
		info.setParameter(parameterName, parameterValue);
		
		return info;
	}
	
	/**
	 * For illegal input handling 
	 * 
	 */
	public static final ErrorInfo getIllegalClientParameterErrorInfo(
			String context, String errorDescription, String userErrorDescription, String parameterName, Object parameterValue) {
		
		ErrorInfo info = new ErrorInfo();
		
		info.setErrorId("IllegalClientParameterError");
		info.setContextId(context);
		
		info.setErrorType(ErrorType.CLIENT_ERROR);
		info.setSeverity(Severity.ERROR);
		
		info.setErrorDescription(errorDescription);
		info.setUserErrorDescription(userErrorDescription);
		
		info.setParameter(parameterName, parameterValue);
		
		return info;
	}
	
	/**
	 * For JDBC communication error handling 
	 * 
	 */
	public static final ErrorInfo getJdbcDaoErrorInfo(
			String context, String errorDescription, Exception e) {
		
		ErrorInfo info = new ErrorInfo();
		
		info.setErrorId("JdbcDaoError");
		info.setContextId(context);
		
		info.setErrorType(ErrorType.SERVICE_ERROR);
		info.setSeverity(Severity.ERROR);
		
		info.setErrorDescription(errorDescription);
		info.setCause(e);
		
		return info;
	}
	
	/**
	 * For URL resource communication errror handling 
	 * 
	 */
	public static final ErrorInfo getUrlDaoErrorInfo(
			String context, String errorDescription, Exception e) {
		
		ErrorInfo info = new ErrorInfo();
		
		info.setErrorId("UrlDaoErrorInfo");
		info.setContextId(context);
		
		info.setErrorType(ErrorType.SERVICE_ERROR);
		info.setSeverity(Severity.ERROR);
		
		info.setErrorDescription(errorDescription);
		info.setCause(e);
		
		return info;
	}

	/**
	 * For unexpected return value handling
	 * 
	 */
	public static final ErrorInfo getIllegalReturnErrorInfo(
			String context, String errorDescription, String userErrorDescription, String parameterName, Object parameterValue) {
		
		ErrorInfo info = new ErrorInfo();
		
		info.setErrorId("IllegalReturnError");
		info.setContextId(context);
		
		info.setErrorType(ErrorType.CLIENT_ERROR);
		info.setSeverity(Severity.ERROR);
		
		info.setErrorDescription(errorDescription);
		info.setUserErrorDescription(userErrorDescription);
		
		info.setParameter(parameterName, parameterValue);
		
		return info;
	}
}
