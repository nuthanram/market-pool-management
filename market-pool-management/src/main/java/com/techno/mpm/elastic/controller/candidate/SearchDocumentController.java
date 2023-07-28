package com.techno.mpm.elastic.controller.candidate;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techno.mpm.response.SuccessResponse;
import com.techno.mpm.service.elastic.candidate.SearchDocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SearchDocumentController {

	
	private static final String SEARCHING_SUCCESSFULL_FOR_THE_PROVIDED_FIELDS = "Searching successfull for the provided fields";
	private final SearchDocumentService searchDocumentService;

	@PostMapping("document/search")
	public ResponseEntity<SuccessResponse> search(@RequestBody Map<String, List<String>> data) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().isError(Boolean.FALSE)
						.message(SEARCHING_SUCCESSFULL_FOR_THE_PROVIDED_FIELDS)
						.data(searchDocumentService.searchDocumentKeyword(data)).build());
	}

	@PostMapping("documents")
	public ResponseEntity<SuccessResponse> searchAll() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().isError(Boolean.FALSE)
						.message(SEARCHING_SUCCESSFULL_FOR_THE_PROVIDED_FIELDS)
						.data(searchDocumentService.searchDocuments()).build());
	}
	
	@DeleteMapping("document/{indexName}")
	public ResponseEntity<SuccessResponse> searchAll(@PathVariable String indexName) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().isError(Boolean.FALSE)
						.message(SEARCHING_SUCCESSFULL_FOR_THE_PROVIDED_FIELDS)
						.data(searchDocumentService.deleteIndex(indexName)).build());
	}

	@PostMapping("countries/cities")
	public ResponseEntity<SuccessResponse> addCities() {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().isError(Boolean.FALSE)
				.message("cities in India added").data(searchDocumentService.addCities()).build());
	}
	
	@GetMapping("countries/cities")
	public ResponseEntity<SuccessResponse> getCities() {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().isError(Boolean.FALSE)
				.message("cities in India fetched").data(searchDocumentService.getCities()).build());
	}

}
