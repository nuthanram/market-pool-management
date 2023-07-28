package com.techno.mpm.audit;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.techno.mpm.exception.UserNotFoundException;

@JsonIgnoreProperties
public class BaseConfigController {

	@Autowired
	private HttpServletRequest httpServletRequest;

	public String getUserId() {
		try {
			final String userId = httpServletRequest != null ? httpServletRequest.getHeader("userId") : "";
			if (userId != null && !userId.equals("null") && !userId.equals("")) {
				return userId;
			} else {
				return "000000000000";
			}
		} catch (Exception e) {
			return "000000000000";
		}
	}

	public String getCandidateId() {
		final String candidateId = httpServletRequest != null ? httpServletRequest.getHeader("candidateId") : "";
		if (candidateId != null && !candidateId.equals("null") && !candidateId.equals("")) {
			return candidateId;
		} else {
			throw new UserNotFoundException("Candidate Id Not Found");
		}
	}

}