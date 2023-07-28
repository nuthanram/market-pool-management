package com.techno.mpm.utils;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techno.mpm.exception.DataNotFoundException;

public class JsonToStringConverter implements AttributeConverter<Object, String> {

	ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

	@Override
	public String convertToDatabaseColumn(Object json) {
		if (json == null)
			return "";
		try {
			return mapper.writeValueAsString(json);
		} catch (JsonProcessingException e) {
			throw new DataNotFoundException(e.getMessage());
		}
	}// End of the convertToDatabaseColumn()

	
	@Override
	public Object convertToEntityAttribute(String dbData) {
		try {
			if (dbData == null || dbData.trim().length() == 0)
				return null;
			return mapper.readValue(dbData, Object.class);
		} catch (JsonProcessingException e) {
			throw new DataNotFoundException(e.getMessage());
		}
	}// End of the convertToEntityAttribute()
}