package com.springernature.watermark.model;

/**
 * Created by Hussein on 13/03/2017.
 */
public class ModelTestUtil {

    public static Book createDummyBook(){
        byte[] file = "test file".getBytes();
        String author = "author";
        String title = "title";

        Book book = (Book)Document.create(file, title, author, DocumentType.BOOK, Topic.BUSINESS);

        return book;
    }

    public static Journal createDummyJournal(){
        byte[] file = "test file".getBytes();
        String author = "author";
        String title = "title";

        Journal journal = (Journal)Document.create(file, title, author, DocumentType.JOURNAL, null);

        return journal;
    }

}
