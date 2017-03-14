package com.springernature.watermark.api;

import com.springernature.watermark.api.resources.*;
import com.springernature.watermark.api.resources.generic.Info;
import com.springernature.watermark.business.WatermarkProcessor;
import com.springernature.watermark.data.DocumentRepository;
import com.springernature.watermark.model.*;
import com.springernature.watermark.model.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import javax.websocket.server.PathParam;
import java.io.IOException;

/**
 * Main class offering the REST API for watermarking service.
 */
@RestController
@RequestMapping(value = "watermarks")
public class WatermarkApi {

    private static Log LOG = LogFactory.getLog(WatermarkApi.class);


    @Autowired
    private DocumentRepository docRepo;
    @Autowired
    private WatermarkProcessor watermarkProcessor;

    /**
     * Receives request to create a watermark for a given document
     * @param file the document to be watermarked
     * @param title title of the document
     * @param topic its topic (an enumerated value)
     * @param content its content (an enumerated value)
     * @param author author of the document
     * @return ID of the saved document, which can be used as a Ticket to poll and retrieve the document later.
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public Long createWatermark(@RequestParam("file") MultipartFile file,
                            @RequestParam("title") String title,
                            @RequestParam(value = "topic", required = false) Topic topic,
                            @RequestParam("content") DocumentType content,
                            @RequestParam("author") String author) {

        if(StringUtils.isEmpty(title) || StringUtils.isEmpty(author)){
            throw new IllegalArgumentException("String query parameters cannot be empty or blank");
        }

        Long id = null;
        try {
            if(file == null || file.getBytes() == null || file.getBytes().length == 0){
                throw new IllegalArgumentException("document file cannot be empty");
            }
            //copy values into model
            Document document = Document.create(file.getBytes(), title, author, content, topic);

            //start processing
            //TODO should be delegated to separate thread?
            id = watermarkProcessor.startProcessingWatermark(document);

        } catch (IOException e) {
            LOG.error("Unexpected IO Exception.", e);
        }

        return id;
    }

    /**
     * Retrieves status of the document without returning the document itself, saving bandwidth and processing resources.
     * Return status is one of:
     * HTTP Status Created (201) if document is available with watermark.
     * HTTP Status Conflict (409) if document is available, but watermark is not appended to it yet.
     * HTTP Status Not Found (404) if document is not found in database.
     * @param documentId
     * @return document with watermark if found, proper HTTP status code otherwise.
     */
    @RequestMapping(value = "{id}",method = RequestMethod.HEAD)
    public ResponseEntity checkWatermark(@PathVariable("id") Long documentId){
        LOG.info("received HEAD request");
        ResponseEntity response = null;

        //check if watermarked already done for given document
        Boolean isWatermarked = watermarkProcessor.checkWatermarkStatus(documentId);
        if(isWatermarked == null){
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("No document was found for the given document ID"));
        }else if (!isWatermarked){
            response = ResponseEntity.status(HttpStatus.CONFLICT).body(new Info("Watermark is not ready yet for the given document ID"));
        }else{
            response = ResponseEntity.status(HttpStatus.CREATED).build();
        }

        LOG.info("Returning HEAD with response status: "+response.getStatusCode());
        return response;
    }

    /**
     * Retrieves a watermarked document (if available), the response will depend on document's state.
     * HTTP Status OK (200) if document is available with watermark.
     * HTTP Status Conflict (409) if document is available, but watermark is not appended to it yet.
     * HTTP Status Not Found (404) if document is not found in database.
     * @param documentId
     * @return document with watermark if found, proper HTTP status code otherwise.
     */
    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public ResponseEntity getDocument(@PathVariable("id") Long documentId){
        LOG.info("received GET request");
        ResponseEntity response = null;

        Document document = watermarkProcessor.getDocument(documentId);
        if(document == null){
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("No document was found for the given document ID"));
        }else if (!document.isWatermarked()){
            response = ResponseEntity.status(HttpStatus.CONFLICT).body(new Info("Watermark is not ready yet for the given document"));
        }else{
            response = ResponseEntity.status(HttpStatus.OK).body(new com.springernature.watermark.api.resources.Document(document));
        }

        LOG.info("Returning GET with response status: "+response.getStatusCode());
        return response;
    }


}
