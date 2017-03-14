package com.springernature.watermark.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Parent class in the Domain/Persistence Model, represents the concept of a Document.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CONTENT")
public abstract class Document {

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private byte[] file;

    private String title;
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false)
    private DocumentType content;

    @Column(name = "IS_WATERMARKED")
    private boolean isWatermarked;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    public Document(){}

    /**
     * Factory method to create the proper implementation of Document's subclasses
     * @param file
     * @param title
     * @param author
     * @param content
     * @param topic
     * @return
     */
    public static Document create(byte[] file, String title, String author, DocumentType content, Topic topic){
        Document doc = null;
        if(content == DocumentType.BOOK){
            doc = new Book(topic);
        }else{
            doc = new Journal();
        }

        doc.setTitle(title);
        doc.setFile(file);
        doc.setAuthor(author);

        return doc;
    }

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        setUpdatedAt(new Date());
        if (getCreatedAt()==null) {
            setCreatedAt(new Date());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isWatermarked() {
        return isWatermarked;
    }

    public void setWatermarked(boolean watermarked) {
        isWatermarked = watermarked;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DocumentType getContent() {
        return content;
    }

    public void setContent(DocumentType content) {
        this.content = content;
    }
}
