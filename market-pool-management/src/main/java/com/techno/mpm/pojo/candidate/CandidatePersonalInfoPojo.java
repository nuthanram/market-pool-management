package com.techno.mpm.pojo.candidate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.techno.mpm.common.StringListConverter;
import com.techno.mpm.entity.candidate.CandidateStatusHistory;
import com.techno.mpm.enumeration.CandidateEnum;
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
public class CandidatePersonalInfoPojo implements Serializable {

	
	private String candidateId;

	private String candidateName;

	private String emailId;

	private String mobileNumber;

	private String highestDegree;

	private Integer age;

	private String panNumber;

	private String adhaarNumber;
	
	private String sourceOfCandidate;

	@Enumerated(EnumType.STRING)
	private CandidateEnum candidateStatus;

	private String course;

	private String instituteName;

	private String averageGrade;

	private String address;

	private String url;

	private String resumeUrl;

	private Boolean isDocumentVerified;

	private Boolean isDelete;

	private String offerInHand;

	@JsonProperty(access = Access.READ_ONLY)
	private Object clientName;

	@JsonProperty(access = Access.READ_ONLY)
	@JsonFormat(shape = Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "Asia/Kolkata")
	private LocalDateTime createdDate;

	@Convert(converter = StringListConverter.class)
	private List<String> others;

	@JsonProperty(access = Access.WRITE_ONLY)
	private CandidateStatusHistory candidateStatusHistory;

	@JsonProperty(access = Access.READ_ONLY)
	@Convert(converter = JsonToStringConverter.class)
	private Object candidateStatusHistories;

	private List<CandiadateEmployentInfoPojo> candidateEmploymentInfos;

	@JsonProperty(access = Access.READ_ONLY)
	private Set<String> technologies;

	@JsonProperty(access = Access.READ_ONLY)
	private List<FeedbackPojo> feedbackPojos;

	@JsonProperty(access = Access.READ_ONLY)
	private List<CandidateInterviewFeedbackPojo> candidateInterviewFeedbackPojos;

	public Boolean getIsDelete() {
		return isDelete == null ? Boolean.FALSE : isDelete;
	}

}
