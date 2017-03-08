package org.rmcc.ccc.controller;

import org.apache.commons.io.IOUtils;
import org.rmcc.ccc.exception.InvalidImageTypeException;
import org.rmcc.ccc.exception.InvalidPathException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlingController {
	
	public class ErrorInfo {
	    public final String url;
	    public String message;

	    public ErrorInfo(String url, Exception ex) {
	        this.url = url;
	        
	        if (ex instanceof InvalidPathException) {	        	
	        	this.message = "No matching Deployment found for Study Area: " + ((InvalidPathException)ex).getStudyAreaName();
	        	this.message += ((InvalidPathException)ex).getLocationId() != null ? 
	        			", and LocationID: " +((InvalidPathException)ex).getLocationId() : "";
	        }
	    }
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidPathException.class)
	@ResponseBody ErrorInfo
	handleBadRequest(HttpServletRequest req, Exception ex) {
		return new ErrorInfo(req.getRequestURL().toString(), ex);
	}

	@ResponseStatus(HttpStatus.ACCEPTED)
	@ExceptionHandler(InvalidImageTypeException.class)
	@ResponseBody byte[]
	handleInvalidImageType(HttpServletRequest req, Exception ex) {
		try {
			return IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("invalidImage.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[]{};
	}
}