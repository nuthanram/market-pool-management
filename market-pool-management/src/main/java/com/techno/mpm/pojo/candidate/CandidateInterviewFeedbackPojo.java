package com.techno.mpm.pojo.candidate;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.techno.mpm.enumeration.InterviewEnum;

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
public class CandidateInterviewFeedbackPojo implements Serializable {

	
	private String candidateFeedbackId;

	private String companyName;

	private String companyAddress;

	private String panelName;

	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
	private LocalDateTime interviewDate;

	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
	private LocalDateTime rescheduleInterviewDate;

	private String roundName;

	private String reason;

	private String remark;
	
	@Enumerated(EnumType.STRING)
	private InterviewEnum interviewStatus;

	private String feedback;

	private String interviewRating;

	private String rating;

	private String candidatePersonalInfoId;
}
