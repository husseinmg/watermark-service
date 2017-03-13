package com.springernature.watermark.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class DocumentTest {

    @Test
    public void testCreateBookSuccess(){
        byte[] file = "test file".getBytes();
        String author = "author";
        String title = "title";

        Document document = Document.create(file, title, author, DocumentType.BOOK, Topic.BUSINESS);

        assertTrue(document instanceof Book);
        assertArrayEquals(file, document.getFile());
        assertEquals(author, document.getAuthor());
        assertEquals(title, document.getTitle());
        assertTrue(DocumentType.BOOK == document.getContent());
        assertTrue(Topic.BUSINESS == ((Book)document).getTopic());
    }

    @Test
    public void testCreateJournalSuccess(){
        byte[] file = "test file".getBytes();
        String author = "author";
        String title = "title";

        Document document = Document.create(file, title, author, DocumentType.JOURNAL, null);

        assertTrue(document instanceof Journal);
        assertArrayEquals(file, document.getFile());
        assertEquals(author, document.getAuthor());
        assertEquals(title, document.getTitle());
        assertTrue(DocumentType.JOURNAL == document.getContent());
    }
}
