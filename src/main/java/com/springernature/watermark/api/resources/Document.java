package com.springernature.watermark.api.resources;

import com.springernature.watermark.model.Book;
import com.springernature.watermark.model.DocumentType;
import com.springernature.watermark.model.Journal;
import com.springernature.watermark.model.Topic;

public class Document {
    private Long id;
    private byte[] file;
    private Watermark watermark;


    public Document(com.springernature.watermark.model.Document in){
        this.setId(in.getId());
        this.setFile(in.getFile());
        this.setWatermark(Watermark.create(in));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Watermark getWatermark() {
        return watermark;
    }

    public void setWatermark(Watermark watermark) {
        this.watermark = watermark;
    }
}
