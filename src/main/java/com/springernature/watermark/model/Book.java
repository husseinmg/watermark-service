package com.springernature.watermark.model;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "BOOK")
public class Book extends Document{

    @Enumerated(EnumType.STRING)
    private Topic topic;

    public Book(){}

    public Book(Topic topic){
        super.setContent(DocumentType.BOOK);
        this.setTopic(topic);
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
