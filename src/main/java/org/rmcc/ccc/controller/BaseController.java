package org.rmcc.ccc.controller;


import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.rmcc.ccc.annotations.Loggable;
import org.rmcc.ccc.model.ErrorInfo;
import org.rmcc.ccc.validator.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseController {
	
	@Autowired
	private MessageSource messageSource;

    @Loggable
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorInfo handleException(HttpServletRequest request, Exception ex) {
        ex.printStackTrace();
        return new ErrorInfo(ex.getMessage(), request.getRequestURL().toString());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationError processValidationError(MethodArgumentNotValidException ex) {
    	BindingResult result = ex.getBindingResult();
    	List<FieldError> fieldErrors = result.getFieldErrors();
    	
    	return processFieldErrors(fieldErrors);
    }
    
    private ValidationError processFieldErrors(List<FieldError> fieldErrors) {
    	ValidationError ve = new ValidationError();
    	
    	for (FieldError e : fieldErrors) {
    		String msg = resolveErrorMessage(e);
    		ve.addFieldError(e.getField(), msg);
    	}
    	
    	return ve;
    }
    
    private String resolveErrorMessage(FieldError fieldError) {
    	String msg = null;
    	try {
    		msg = messageSource.getMessage(fieldError, Locale.getDefault());
    	} catch (NoSuchMessageException e) {
    		msg = fieldError.getCode();
    	}
    	
    	return msg;
    }
}
