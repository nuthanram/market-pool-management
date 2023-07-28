package com.techno.mpm.document.candidate;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(indexName = "mpm_city_document")
public class CityDocument {

	
	@Id
	@Field(name = "cd_city_id")
	private String cityId;

	@Field(name = "cd_city_names",type = FieldType.Object)
	private Object data;
}
