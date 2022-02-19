package com.example.aerospikeembedded.repo;

import com.example.aerospikeembedded.config.AerospikeCacheObject;
import org.springframework.data.aerospike.repository.AerospikeRepository;

public interface  CustomerAerospikeRepo <T> extends AerospikeRepository<AerospikeCacheObject<T>, Long> {
}
