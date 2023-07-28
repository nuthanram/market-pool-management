package com.techno.mpm.pojo.candidate;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
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
//@JsonInclude(value = Include.NON_DEFAULT)
public class CandidateFilterPojo implements Serializable {

	
	@Enumerated(EnumType.STRING)
	private CandidateEnum candidateEnum;
	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private LocalDate fromDate;
	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private LocalDate toDate;
}
