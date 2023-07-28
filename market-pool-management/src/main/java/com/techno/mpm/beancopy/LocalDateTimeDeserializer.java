package com.techno.mpm.beancopy;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
	

	@Override
	public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String asString = json.getAsString();
		if (asString.contains("T")) {
			LocalDateTime parse = LocalDateTime.parse(json.getAsString());
			asString = parse.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
		}
		return json == null ? null
				: LocalDateTime.parse(asString,
						DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withLocale(Locale.ENGLISH));
	}
}
