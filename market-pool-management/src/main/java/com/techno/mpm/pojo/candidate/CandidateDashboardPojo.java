package com.techno.mpm.pojo.candidate;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class CandidateDashboardPojo implements Serializable {

	
	private Long totalCandidate;
	private Long available;
	private Long notAvailable;
	private Long selected;
	private Long deployed;
	private List<CandidatePersonalInfoPojo> candidatePersonalInfoPojos;
}
