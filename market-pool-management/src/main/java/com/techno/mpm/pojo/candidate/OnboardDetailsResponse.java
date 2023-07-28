package com.techno.mpm.pojo.candidate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardDetailsResponse {

	private Boolean isError;
	private Object data;
	private Boolean isAvailable;
	private String message;

}
