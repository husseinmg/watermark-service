package com.springernature.watermark.business;

import com.springernature.watermark.api.WatermarkApi;
import com.springernature.watermark.data.DocumentRepository;
import com.springernature.watermark.model.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Contains some of the business and data access logic, it actually works as a Business Delegate between API classes
 * and the rest of the application.
 */
@Component
public class WatermarkProcessor {
    private static Log LOG = LogFactory.getLog(WatermarkApi.class);

    @Autowired
    DocumentRepository documentRepository;

    @Value("${com.springernature.watermark.watermarkprocessor.secondstowait}")
    int secondsToWait;

    /**
     * Start processing the document, including saving it first in DB.
     * @param doc
     * @return
     */
    public Long startProcessingWatermark(Document doc){
        LOG.info("starting document processing");
        //save document to DB
        Long id = documentRepository.save(doc).getId();

        LOG.info("document saved");

        //Simulate time expensive watermarking work
        simulateWatermarkProcessing(doc.getId());



        return id;
    }

    /**
     * Checks watermarking status
     * @param documentId
     * @return true if watermark is created for document with given ID
     */
    public Boolean checkWatermarkStatus(Long documentId){
        return documentRepository.getIsWatermarkedById(documentId);
    }

    /**
     * Retrieve a document from database
     * @param documentId
     * @return
     */
    public Document getDocument(Long documentId){
        LOG.info("retrieving document");

        Document document = documentRepository.findOne(documentId);

        return document;
    }

    /**
     * Method simulates calling an external service that does the actual watermarking (may be an exe file or something).
     * @param id
     */
    private void simulateWatermarkProcessing(Long id){
            LOG.info("Simulating Watermark processing with secondsToWait = "+secondsToWait);

            if(secondsToWait > 0) {
                new Thread(((Runnable) () -> {
                    try {
                        Thread.sleep(secondsToWait * 1000);
                        Document doc = documentRepository.findOne(id);
                        if (doc != null) {
                            doc.setWatermarked(true);
                            documentRepository.save(doc);
                            LOG.info("Watermark created and updated in database.");
                        }
                    } catch (InterruptedException e) {
                        LOG.error(e);
                    }
                })).start();
            }else {
                Document doc = documentRepository.findOne(id);
                if (doc != null) {
                    doc.setWatermarked(true);
                    documentRepository.save(doc);
                    LOG.info("Watermark created and updated in database.");
                }
            }
    }

}
