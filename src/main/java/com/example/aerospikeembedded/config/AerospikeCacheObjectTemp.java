package com.example.aerospikeembedded.config;

import lombok.Data;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.annotation.PersistenceConstructor;


/**
 *  set name for records will be : AerospikeCacheObjectTemp
 *  to be use only for testing/comparing aerospike changes in prod
 */
@Document
//@Builder
@Data
public class AerospikeCacheObjectTemp<T> extends AerospikeCacheObject<T> {

    @PersistenceConstructor
    public AerospikeCacheObjectTemp() {
    }

    public AerospikeCacheObjectTemp(long id,T data) {
        super(id,data);
    }



}
