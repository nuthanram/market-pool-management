package com.techno.mpm.entity.candidate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.techno.mpm.audit.Audit;
import com.techno.mpm.enumeration.CandidateEnum;
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
@Table(name = "mpm_candidate_feedback_history")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "feedbackId")
public class FeedbackHistory extends Audit {
	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feedback_history_seq")
	@GenericGenerator(name = "feedback_history_seq", strategy = "com.techno.mpm.utils.SequenceGenerator", parameters = {
			@Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = SequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%06d") })
	@Column(name = "cfh_feedback_id")
	private String feedbackId;
	@Column(name = "cfh_feedback_status")
	private CandidateEnum candidateStatus;
	@Column(name = "cfh_feedback_reason")
	private String reason;

	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "cfh_candidate_id")
	private CandidatePersonalInfo candidatePersonalInfo;
}
