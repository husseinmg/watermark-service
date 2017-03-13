package com.springernature.watermark.api;

import java.net.URI;
import java.util.Map;

import com.springernature.watermark.Application;
import com.springernature.watermark.model.Book;
import com.springernature.watermark.model.ModelTestUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class WatermarkApiIntegrationTest {

    private static Log LOG = LogFactory.getLog(WatermarkApi.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String baseUrl;

    @PostConstruct
    public void setup(){
        baseUrl = "http://localhost:" + this.port + "/watermarks";
    }

    @Test
    public void successfulPost() throws Exception {
        LOG.info("Testing successfulPost");
        Book book = ModelTestUtil.createDummyBook();
        Long id = postRequest(book);

        assertTrue(id.longValue() > 0);
    }

    @Test
    public void missingParameterPost() throws Exception {
        LOG.info("Testing missingParameterPost");
        Book book = ModelTestUtil.createDummyBook();
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
        map.add("file", book.getFile());
        map.add("author", book.getAuthor());
        map.add("title", book.getTitle());
        map.add("topic", book.getTopic().name());

        map.add("file", new ClassPathResource("test.txt"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);

        Map response = this.testRestTemplate.postForObject(baseUrl, requestEntity, Map.class);

        assertEquals(400, response.get("status"));
    }

    @Test
    public void faultyParameterPost() throws Exception {
        LOG.info("Testing faultyParameterPost");
        Book book = ModelTestUtil.createDummyBook();
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
        map.add("file", book.getFile());
        map.add("author", book.getAuthor());
        map.add("title", book.getTitle());
        map.add("content", book.getContent().name());
        map.add("topic", "randomTopic");

        map.add("file", new ClassPathResource("test.txt"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);

        ResponseEntity<String> response = this.testRestTemplate.postForEntity(baseUrl, requestEntity, String.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void successfulHead() throws Exception {
        LOG.info("Testing successfulHead");
        Book book = ModelTestUtil.createDummyBook();
        Long id = postRequest(book);
        Thread.yield();

        RequestEntity entity = RequestEntity.head(new URI(baseUrl+"/"+id)).build();
        ResponseEntity<Map> response = this.testRestTemplate.exchange(entity, Map.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void successfulGet() throws Exception {
        LOG.info("Testing successfulGet");
        Book book = ModelTestUtil.createDummyBook();
        Long id = postRequest(book);
        Thread.yield();

        RequestEntity entity = RequestEntity.get(new URI(baseUrl+"/"+id)).build();
        ResponseEntity<Map> response = this.testRestTemplate.exchange(entity, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private Long postRequest(Book book) {
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
        map.add("file", book.getFile());
        map.add("author", book.getAuthor());
        map.add("title", book.getTitle());
        map.add("content", book.getContent().name());
        map.add("topic", book.getTopic().name());

        map.add("file", new ClassPathResource("test.txt"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);

        return this.testRestTemplate.postForObject(baseUrl, requestEntity, Long.class);
    }
}
