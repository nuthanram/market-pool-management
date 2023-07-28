package com.techno.mpm.elastic.repository.candidate;

import java.util.List;
import java.util.Optional;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.techno.mpm.document.candidate.CandidateDocument;

public interface DocumentRepository extends ElasticsearchRepository<CandidateDocument, String> {
	
	List<CandidateDocument> findByResumeContentLike(String resumeContent);

	Optional<CandidateDocument> findByCandidateId(String candidateId);

	@Query("{\"bool\": {\"must\": [{\"term\": {\"candidateId\": \"?0\"}}]}}")
	List<CandidateDocument> findByField(String fieldValue);
}
