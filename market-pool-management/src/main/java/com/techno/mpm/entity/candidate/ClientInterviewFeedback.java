package com.techno.mpm.entity.candidate;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.techno.mpm.enumeration.InterviewEnum;
import com.techno.mpm.utils.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "mpm_candidate_interview_feedback")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "candidateFeedbackId")
public class ClientInterviewFeedback{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidate_interview_feedback_seq")
	@GenericGenerator(name = "candidate_interview_feedback_seq", strategy = "com.techno.mpm.utils.SequenceGenerator", parameters = {
			@Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = SequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%06d") })
	@Column(name = "cif_candidate_feedback_id")
	private String candidateFeedbackId;

	@Column(name = "cif_company_name")
	private String companyName;

	@Column(name = "cif_company_address")
	private String companyAddress;

	@Column(name = "cif_panel_name")
	private String panelName;

	@Column(name = "cif_interview_date")
	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
	private LocalDateTime interviewDate;

	@Column(name = "cif_reschedule_interview_date")
	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
	private LocalDateTime rescheduleInterviewDate;

	@Column(name = "cif_round_name", length = 50)
	private String roundName;

	@Column(name = "cif_reason", length = 999)
	private String reason;

	@Column(name = "cif_remark", length = 999)
	private String remark;

	@Column(name = "cif_interview_status")
	@Enumerated(EnumType.STRING)
	private InterviewEnum interviewStatus;

	@Column(name = "cif_feedback")
	private String feedback;

	@Column(name = "cif_interview_rating")
	private String interviewRating;

	@Column(name = "cif_is_delete")
	private Boolean isDelete;

	@Column(name = "cif_rating")
	private String rating;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "cf_candidate_id")
	private CandidatePersonalInfo candidatePersonalInfo;
}
