package com.techno.mpm.document.candidate;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.techno.mpm.common.StringListConverter;
import com.techno.mpm.enumeration.CandidateEnum;
import com.techno.mpm.utils.JsonToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(indexName = "mpm_candidate_document")
public class CandidateDocument {
	
	@Id
//	@Field(name = "cpi_id")
	private String id;

// 	@Field(name = "cpi_candidate_id")
	private String candidateId;

//	@Field(name = "cpi_candidate_name")
	private String candidateName;

//	@Field(name = "cpi_email_id")
	private String emailId;

//	@Field(name = "cpi_mobile_number")
	private String mobileNumber;

//	@Field(name = "cpi_highest_degree")
	private String highestDegree;

//	@Field(name = "cpi_age")
	private Integer age;

	private String panNumber;

	private String adhaarNumber;
	
	private String sourceOfCandidate;

//	@Field(name = "cpi_candidate_status")
	@Enumerated(EnumType.STRING)
	private CandidateEnum candidateStatus;

//	@Field(name = "cpi_course")
	private String course;

//	@Field(name = "cpi_institute_name")
	private String instituteName;

//	@Field(name = "cpi_average_grade")
	private String averageGrade;

//	@Field(name = "cpi_address")
	private String address;

//	@Field(name = "cpi_resume_url")
	private String resumeUrl;

//	@Field(name = "cpi_is_document_verified")
	private Boolean isDocumentVerified;

//	@Field(name = "cpi_is_delete")
	private Boolean isDelete;

//	@Field(name = "cpi_offer_in_hand")
	private String offerInHand;

//	@Field(name = "cpi_resume_content")
	private String resumeContent;

//	@Field(name = "cpi_others")
	@Convert(converter = StringListConverter.class)
	private List<String> others;

	@Convert(converter = JsonToStringConverter.class)
	private Object candidateStatusHistories;	

//	@Field(type = FieldType.Nested)
	private List<CandidateEmploymentDocument> candidateEmploymentInfos;
	
}
