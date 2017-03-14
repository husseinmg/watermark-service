package com.springernature.watermark.api.resources.generic;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Error {
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long documentId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorCode;

    public Error(String message){
        this.message = message;
    }

    public Error(String message, Long documentId){
        this.message = message;
        this.documentId = documentId;
    }

    public Error(String message, Long documentId, String errorCode) {
        this.message = message;
        this.documentId = documentId;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
