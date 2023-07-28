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
public class CandidateSkillPojo implements Serializable {

	
	private Long frontEnd;
	private Long backEnd;
	private Long fullStack;
	private Long reactJs;
	private Long angularJs;
	private Long vueJs;
	private Long javaSpringBoot;
	private Long nodeExpress;
	private Long javaFullStackReact;
	private Long javaFullStackAngular;
	private Long meanStack;
	private Long mernStack;
	private List<CandidatePersonalInfoPojo> candidatePersonalInfoPojo;
}
