package com.springernature.watermark.api.resources;

import com.springernature.watermark.model.*;
import com.springernature.watermark.model.Document;

public abstract class Watermark {
    private String title;
    private String author;
    private DocumentType content;

    public Watermark(Document in){
        this.title = in.getTitle();
        this.author = in.getAuthor();
        this.content = in.getContent();
    }

    /**
     * Factory method to create the proper implementation of Watermark
     * @param in
     * @return
     */
    public static Watermark create(Document in){
        if(in instanceof Book){
            return new BookWatermark(in);
        }else{
            return new JournalWatermark(in);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public DocumentType getContent() {
        return content;
    }

    public void setContent(DocumentType content) {
        this.content = content;
    }
}
