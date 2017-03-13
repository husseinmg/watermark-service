package com.springernature.watermark.business;

import com.springernature.watermark.data.DocumentRepository;
import com.springernature.watermark.model.Document;
import com.springernature.watermark.model.ModelTestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WatermarkProcessorTest {
    @Mock
    DocumentRepository documentRepository;

    @InjectMocks
    private WatermarkProcessor watermarkProcessor;

    @Test
    public void testStartProcessingWatermarkSuccess(){
        Document book = ModelTestUtil.createDummyBook();

        when(documentRepository.save(any(Document.class))).thenReturn(book);
        watermarkProcessor.startProcessingWatermark(book);
    }

    @Test
    public void testCheckWatermarkStatus(){
        when(documentRepository.getIsWatermarkedById(anyLong())).thenReturn(false);
        boolean status = watermarkProcessor.checkWatermarkStatus(anyLong());

        assertFalse(status);
    }

    @Test
    public void testGetDocument(){
        Document book = ModelTestUtil.createDummyBook();
        when(documentRepository.findOne(anyLong())).thenReturn(book);

        Document document = watermarkProcessor.getDocument(anyLong());

        assertSame(document, book);
    }

}
