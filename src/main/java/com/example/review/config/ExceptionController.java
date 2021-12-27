package com.example.review.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice(basePackages = {"com.example.review"})
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    protected ResponseResult defaultException(Exception e) {
        log.error(e.getMessage());
        return new ResponseResult(e.getMessage());
    }

    @ExceptionHandler(TripleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ResponseResult handleMethodCustom400Exception(TripleException e) {
        log.error(e.getMessage());
        return new ResponseResult(e.getCode());
    }
}