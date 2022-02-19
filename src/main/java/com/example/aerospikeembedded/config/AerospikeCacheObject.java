package com.example.aerospikeembedded.config;

import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Document
@Builder
@Data
@AllArgsConstructor
public class AerospikeCacheObject<T> {
	@PersistenceConstructor
	public AerospikeCacheObject() {
		this.id = null;
		this.data = null;
	}

	@Id
	private Long id;

	@Field("data")
	private T data;
}