package com.springernature.watermark.data;

import com.springernature.watermark.model.Document;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Main persistence interface of the application
 */
public interface DocumentRepository extends CrudRepository<Document, Long> {

    /**
     * get watermarking status for document with given ID.
     * @param id
     * @return
     */
    @Query("select isWatermarked from Document where id =:id")
    Boolean getIsWatermarkedById(@Param("id") Long id);

}
