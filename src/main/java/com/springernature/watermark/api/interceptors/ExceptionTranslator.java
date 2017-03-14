package com.springernature.watermark.api.interceptors;

import com.springernature.watermark.api.resources.generic.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Provides aspect oriented exception handling to REST API
 */
@ControllerAdvice
public class ExceptionTranslator {

    /**
     * Handle parameters not matching the specified declared types in REST API method signatures.
     * @param e
     * @return response to return to API clients.
     */
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
        return ResponseEntity.unprocessableEntity().body(new Error(message));
    }

    /**
     * Handle parameters not matching the expected formatting or other validation conditions.
     * @param e
     * @return response to return to API clients.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(IllegalArgumentException e) {
        return ResponseEntity.unprocessableEntity().body(new Error(e.getMessage()));
    }
}