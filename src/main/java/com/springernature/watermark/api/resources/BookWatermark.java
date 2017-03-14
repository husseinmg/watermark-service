package com.springernature.watermark.api.resources;

import com.springernature.watermark.model.*;
import com.springernature.watermark.model.Document;

/**
 * Concrete implementation for Book Watermarks
 */
public class BookWatermark extends Watermark{
    private Topic topic;

    public BookWatermark(com.springernature.watermark.model.Document in){
        super(in);
        this.setContent(DocumentType.BOOK);
        this.setTopic(((Book)in).getTopic());
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
