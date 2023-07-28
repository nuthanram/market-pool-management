package com.techno.mpm.pojo.candidate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.techno.mpm.common.StringListConverter;
import com.techno.mpm.utils.JsonToStringConverter;

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
public class CandiadateEmployentInfoPojo implements Serializable {

	private String employmentId;

	private String companyName;

	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private LocalDate startDate;

	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private LocalDate endDate;

	private Double yoe;

	private Double ayoe;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
	private LocalDateTime autualExperienceDate;

	private Integer noticePeriod;

	private String designation;

	private String currentLocation;

	private List<String> prefferedLocation;

	private String jobDescription;

	private BigDecimal currentSalary;

	private BigDecimal expectedSalary;

	@Convert(converter = StringListConverter.class)
	private List<String> skillSet;

	@Convert(converter = JsonToStringConverter.class)
	private Object referencePersonDetails;

	// private CandidatePersonalInfoPojo candidatePersonalInfo;
}
