package com.techno.mpm.entity.candidate;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.techno.mpm.enumeration.CandidateEnum;
import com.techno.mpm.enumeration.InterviewEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CandidateStatusHistory {

	
	private CandidateEnum candidateEnum;
	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	@Field(type = FieldType.Date, format = DateFormat.date)
	private LocalDate date;
	private String description;
	private String candidateFeedbackId;

	private String companyName;
	
	private String type;

	private String panelName;

	private String roundName;

	private String reason;

	private String remark;
	
	private String feedback;

//	private String interviewRating;

	private String rating;
	
}
