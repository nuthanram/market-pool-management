package com.techno.mpm.pojo.candidate;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.techno.mpm.enumeration.CandidateEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(value = Include.NON_DEFAULT)
public class FeedbackPojo implements Serializable {

	
	private String candidateId;
	private String feedbackId;
	private CandidateEnum candidateStatus;
	private String reason;
}
