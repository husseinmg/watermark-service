package com.springernature.watermark.api.resources.generic;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Info {
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long documentId;

    public Info(String message){
        this.message = message;
    }

    public Info(String message, Long documentId){
        this.message = message;
        this.documentId = documentId;
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
}
