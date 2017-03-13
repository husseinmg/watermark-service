package com.springernature.watermark.api.interceptors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Class<?> type = e.getRequiredType();
        String message;
        if (type.isEnum()) {
            message = "The parameter " + e.getName() + " must have a value among : " + StringUtils.arrayToDelimitedString(type.getEnumConstants(), ", ");
        } else {
            message = "The parameter " + e.getName() + " must be of type " + type.getName();
        }
        return ResponseEntity.unprocessableEntity().body(message);
    }
}