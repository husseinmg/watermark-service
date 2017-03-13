package com.springernature.watermark.api.resources;

import com.springernature.watermark.model.*;

public class JournalWatermark extends Watermark {

    public JournalWatermark(com.springernature.watermark.model.Document in){
        super(in);
        this.setContent(DocumentType.JOURNAL);
    }
}
