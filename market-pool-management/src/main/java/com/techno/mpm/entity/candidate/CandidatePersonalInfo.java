package com.techno.mpm.entity.candidate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.techno.mpm.audit.Audit;
import com.techno.mpm.common.StringListConverter;
import com.techno.mpm.enumeration.CandidateEnum;
import com.techno.mpm.utils.JsonToStringConverter;
import com.techno.mpm.utils.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "mpm_candidate_personal_info")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "candidateId")
public class CandidatePersonalInfo extends Audit implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidate_personal_seq")
	@GenericGenerator(name = "candidate_personal_seq", strategy = "com.techno.mpm.utils.SequenceGenerator", parameters = {
			@Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = SequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%06d") })
	@Column(name = "cpi_candidate_id")
	private String candidateId;

	@Column(name = "cpi_candidate_name", nullable = false, length = 50)
	private String candidateName;

	@Column(name = "cpi_email_id", unique = true, nullable = false, length = 100)
	private String emailId;

	@Column(name = "cpi_mobile_number", unique = true, nullable = false, precision = 19)
	private String mobileNumber;

	@Column(name = "cpi_highest_degree", length = 50)
	private String highestDegree;

	@Column(name = "cpi_age")
	private Integer age;

	@Column(name = "cpi_pan_number", length = 20)
	private String panNumber;

	@Column(name = "cpi_adhaar_number", length = 20)
	private String adhaarNumber;
	
	@Column(name = "cpi_source_of_candidate", length = 200)
	private String sourceOfCandidate;
	
	@Column(name = "cpi_candidate_status")
	@Enumerated(EnumType.STRING)
	private CandidateEnum candidateStatus;

	@Column(name = "cpi_course", length = 50)
	private String course;

	@Column(name = "cpi_institute_name", length = 50)
	private String instituteName;

	@Column(name = "cpi_average_grade", length = 10)
	private String averageGrade;

	@Column(name = "cpi_address", length = 255)
	private String address;

	@Column(name = "cpi_resume_url", length = 255)
	private String resumeUrl;

	@Column(name = "cpi_is_document_verified", precision = 3)
	private Boolean isDocumentVerified;

	@Column(name = "cpi_is_delete")
	private Boolean isDelete;

	@Column(name = "cpi_offer_in_hand")
	private String offerInHand;

	@Column(name = "cpi_others", length = 999)
	@Convert(converter = StringListConverter.class)
	private List<String> others;

	@Column(name="candidate_status_history",length = 9999,columnDefinition = "TEXT")
	@Convert(converter = JsonToStringConverter.class)
	private Object candidateStatusHistories;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "candidatePersonalInfo", fetch = FetchType.LAZY)
	private List<ClientInterviewFeedback> candidateInterviewFeedbacks;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "candidatePersonalInfo", fetch = FetchType.LAZY)
	private List<CandidateEmploymentInfo> candidateEmploymentInfos;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "candidatePersonalInfo", fetch = FetchType.LAZY)
	private List<FeedbackHistory> feedbackHistories;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of("role").stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return getEmailId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
