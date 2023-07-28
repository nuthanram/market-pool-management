package com.techno.mpm.elastic.repository.candidate;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.techno.mpm.document.candidate.CityDocument;

public interface CityDocumentRepository extends ElasticsearchRepository<CityDocument, String> {
	
}
