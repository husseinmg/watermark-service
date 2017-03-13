package com.springernature.watermark.api.resources;

import com.springernature.watermark.model.Book;
import com.springernature.watermark.model.DocumentType;
import com.springernature.watermark.model.Journal;
import com.springernature.watermark.model.ModelTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class DocumentTest {

    @Test
    public void testConstructorForBook(){
        Book book = ModelTestUtil.createDummyBook();

        Document document = new Document(book);

        assertTrue(document.getWatermark() instanceof BookWatermark);
        assertNotNull(document.getFile());
        assertNotNull(document.getWatermark().getAuthor());
        assertEquals(document.getWatermark().getContent(), DocumentType.BOOK);
        assertNotNull(document.getWatermark().getTitle());
        assertNotNull(((BookWatermark)document.getWatermark()).getTopic());
    }

    @Test
    public void testConstructorForJournal(){
        Journal journal = ModelTestUtil.createDummyJournal();

        Document document = new Document(journal);

        assertTrue(document.getWatermark() instanceof JournalWatermark);
        assertNotNull(document.getFile());
        assertNotNull(document.getWatermark().getAuthor());
        assertEquals(document.getWatermark().getContent(), DocumentType.JOURNAL);
        assertNotNull(document.getWatermark().getTitle());
    }
}
