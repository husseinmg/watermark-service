package com.springernature.watermark.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "JOURNAL")
public class Journal extends Document{

    public Journal(){
        super.setContent(DocumentType.JOURNAL);
    }

}
