package org.rmcc.ccc.controller;


import org.rmcc.ccc.annotations.Loggable;
import org.rmcc.ccc.model.ErrorInfo;
import org.rmcc.ccc.validator.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	private MessageSource messageSource;

    @Loggable
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorInfo handleException(HttpServletRequest request, Exception ex) {
        LOGGER.error("Controller exception occurred", ex);
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
        String msg;
        try {
            msg = messageSource.getMessage(fieldError, Locale.getDefault());
    	} catch (NoSuchMessageException e) {
    		msg = fieldError.getCode();
    	}
    	
    	return msg;
    }
}
