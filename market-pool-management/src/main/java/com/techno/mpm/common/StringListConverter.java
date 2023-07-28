package com.techno.mpm.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.AttributeConverter;

public class StringListConverter implements AttributeConverter<List<String>, String> {
	
	private static final String SPLIT_CHAR = ";";

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		return attribute != null && !attribute.isEmpty() ? String.join(SPLIT_CHAR, attribute) : "";
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		return dbData != null ? Arrays.asList(dbData.split(SPLIT_CHAR)) : Collections.emptyList();
	}

}
