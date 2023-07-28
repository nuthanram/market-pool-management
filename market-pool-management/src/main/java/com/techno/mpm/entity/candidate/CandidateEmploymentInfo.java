package com.techno.mpm.entity.candidate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.techno.mpm.audit.Audit;
import com.techno.mpm.common.StringListConverter;
import com.techno.mpm.utils.JsonToStringConverter;
import com.techno.mpm.utils.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "mpm_candidate_employment_info")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "employmentId")
public class CandidateEmploymentInfo extends Audit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidate_employment_seq")
	@GenericGenerator(name = "candidate_employment_seq", strategy = "com.techno.mpm.utils.SequenceGenerator", parameters = {
			@Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = SequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%06d") })
	@Column(name = "cei_employment_id")
	private String employmentId;

	@Column(name = "cei_company_name", length = 50)
	private String companyName;

	@Column(name = "cei_start_date")
	private LocalDate startDate;

	@Column(name = "cei_end_date")
	private LocalDate endDate;

	@Column(name = "cei_yoe")
	private Double yoe;
	
	@Column(name = "cei_ayoe")
	private Double ayoe;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
	private LocalDateTime autualExperienceDate;

	@Column(name = "cei_notice_period")
	private Integer noticePeriod;

	@Column(name = "cei_designation", length = 50)
	private String designation;

	@Column(name = "cei_current_location", length = 250)
	private String currentLocation;

	@Column(name = "cei_preffered_location", length = 250)
	@Convert(converter = StringListConverter.class)
	private List<String> prefferedLocation;
	
	@Column(name = "cei_job_description", length = 999)
	private String jobDescription;

	@Column(name = "cei_current_salary", precision = 10, scale = 2)
	private BigDecimal currentSalary;

	@Column(name = "cei_expected_salary", precision = 10, scale = 2)
	private BigDecimal expectedSalary;

	@Column(name = "cei_skill_set", length = 250)
	@Convert(converter = StringListConverter.class)
	private List<String> skillSet;

	@Column(name = "cei_reference_person_details", length = 999)
	@Convert(converter = JsonToStringConverter.class)
	private Object referencePersonDetails;

	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "cei_candidate_id")
	private CandidatePersonalInfo candidatePersonalInfo;
}
