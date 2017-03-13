package com.springernature.watermark.business;

import com.springernature.watermark.api.WatermarkApi;
import com.springernature.watermark.data.DocumentRepository;
import com.springernature.watermark.model.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WatermarkProcessor {
    private static Log LOG = LogFactory.getLog(WatermarkApi.class);

    @Autowired
    DocumentRepository documentRepository;

    @Value("${com.springernature.watermark.watermarkprocessor.secondstowait}")
    int secondsToWait;

    public Long startProcessingWatermark(Document doc){
        LOG.info("starting document processing");
        //save document to DB
        Long id = documentRepository.save(doc).getId();

        LOG.info("document saved");

        //TODO handle error cases in this extensive work?
        //Simulate time expensive watermarking work
        new Thread(((Runnable) () -> waterMarkDocument(doc.getId()))).start();


        return id;
    }

    public Boolean checkWatermarkStatus(Long documentId){
        return documentRepository.getIsWatermarkedById(documentId);
    }

    public Document getDocument(Long documentId){
        LOG.info("retrieving document");

        Document document = documentRepository.findOne(documentId);

        return document;
    }

    private void waterMarkDocument(Long id){
        try {
            LOG.info("Simulating Watermark processing with secondsToWait = "+secondsToWait);

            Thread.sleep(secondsToWait * 1000);

            Document doc = documentRepository.findOne(id);
            if(doc != null) {
                doc.setWatermarked(true);
                documentRepository.save(doc);
                LOG.info("Watermark created and updated in database.");
            }
        } catch (InterruptedException e) {
            LOG.error(e);
        }
    }

}
