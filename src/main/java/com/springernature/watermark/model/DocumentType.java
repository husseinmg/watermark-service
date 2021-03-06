package com.springernature.watermark.model;

/**
 * Allowable document types
 */
public enum DocumentType {
    BOOK('B'),
    JOURNAL('J');

    public Character value;

    DocumentType(Character value){
        this.value = value;
    }
}
