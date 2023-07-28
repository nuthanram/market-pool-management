package com.techno.mpm.auth.dto;

import java.util.Map;

import javax.persistence.Convert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.techno.mpm.utils.JsonToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class AuthenticationResponse {
	

	private Long data;

	private boolean isVarifyToken;
	
	@Convert(converter = JsonToStringConverter.class)
	private Object userLoginHistory;

	private Map<String, String> roles;

	private String accessToken;

	private String refreshToken;

}
