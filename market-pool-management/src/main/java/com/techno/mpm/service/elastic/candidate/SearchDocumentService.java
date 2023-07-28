package com.techno.mpm.service.elastic.candidate;

import java.util.List;
import java.util.Map;

public interface SearchDocumentService {
	public Object searchDocumentKeyword(Map<String, List<String>> data);

	public Object searchDocuments();

	public String addCities();

	public Object getCities();

	public String deleteIndex(String indexName);
}
