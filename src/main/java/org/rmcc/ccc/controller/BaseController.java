package org.rmcc.ccc.controller;


import org.rmcc.ccc.annotations.Loggable;
import org.rmcc.ccc.model.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    @Loggable
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorInfo handleException(HttpServletRequest request, Exception ex) {
        ex.printStackTrace();
        return new ErrorInfo(ex.getMessage(), request.getRequestURL().toString());
    }
}
