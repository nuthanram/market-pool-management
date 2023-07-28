package com.techno.mpm.service.elastic.candidate;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.techno.mpm.document.candidate.CandidateDocument;
import com.techno.mpm.document.candidate.CityDocument;
import com.techno.mpm.elastic.repository.candidate.CityDocumentRepository;
import com.techno.mpm.elastic.repository.candidate.DocumentRepository;
import com.techno.mpm.exception.DataNotFoundException;
import com.techno.mpm.response.CityResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@SuppressWarnings("deprecation")
public class SearchDocumentServiceImpl implements SearchDocumentService {

	@Autowired
	private ElasticsearchRestTemplate elasticsearchRestTemplate;
	
	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private CityDocumentRepository cityDocumentRepository;

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	BoolQueryBuilder boolQueryBuilders;

	List<String> keys = List.of("candidateEmploymentInfos.yoe", "candidateEmploymentInfos.expectedSalary",
			"candidateEmploymentInfos.currentSalary", "candidateEmploymentInfos.noticePeriod",
			"candidateEmploymentInfos.companyName", "age");

	public Object searchDocumentKeyword(Map<String, List<String>> data) {
		try {
			boolQueryBuilders = QueryBuilders.boolQuery();
			data.forEach(
					(fieldName, fieldValue) -> fieldValue.stream().forEach(dataValue -> boolQueryBuilders = Optional
							.ofNullable(keys.contains(fieldName)).filter(field -> field).map(value -> {
								String[] split = dataValue.split("\\-");
								if (fieldName.equalsIgnoreCase("candidateEmploymentInfos.companyName")) {
									return boolQueryBuilders
											.mustNot(QueryBuilders.matchPhrasePrefixQuery(fieldName, dataValue));
								}
								return boolQueryBuilders
										.must(QueryBuilders.rangeQuery(fieldName).gte(split[0]).lt(split[1]));
							}).orElseGet(() -> boolQueryBuilders
									.must(QueryBuilders.matchPhrasePrefixQuery(fieldName, dataValue)))));
			NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(boolQueryBuilders).build();

			return List
					.of(elasticsearchRestTemplate.search(build, CandidateDocument.class,
							IndexCoordinates.of("mpm_candidate_document")))
					.stream()
					.map(doc -> doc.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList()))
					.flatMap(Collection::stream).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataNotFoundException(e.getMessage(), e);
		}
	}

	public Object searchDocuments() {
		return documentRepository.findAll();
	}

	public String addCities() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://countriesnow.space/api/v0.1/countries/cities/q?country=india";
		CityResponse response = restTemplate.getForObject(url, CityResponse.class);
		cityDocumentRepository.save(CityDocument.builder().cityId("1").data(response.getData()).build());
		return "All Cities Added";
	}

	Object cities;

	public Object getCities() {
		cities = null;
		cityDocumentRepository.findAll().forEach(x -> cities = x.getData());
		return cities;
	}

	public String deleteIndex(String indexName) {
		DeleteIndexRequest request = new DeleteIndexRequest(indexName);
		try {
			AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
			if (response.isAcknowledged()) {
				log.info("Index deleted successfully");
				return "Index deleted successfully";
			} else {
				log.info("Failed to delete index");
				return "Failed to delete index";
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new DataNotFoundException(e.getMessage());
		}
	}
}
