package com.techno.mpm.document.candidate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.Convert;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.techno.mpm.common.StringListConverter;
import com.techno.mpm.utils.JsonToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CandidateEmploymentDocument {

//	@Field(name = "cei_employment_id")
	private String employmentId;

//	@Field(name = "cei_company_name")
	private String companyName;

//	@Field(name = "cei_start_date")
	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	@Field(type = FieldType.Date, format = DateFormat.date)
	private LocalDate startDate;

//	@Field(name = "cei_end_date")
	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	@Field(type = FieldType.Date, format = DateFormat.date)
	private LocalDate endDate;

//	@Field(name = "cei_yoe")
	private Double yoe;

	private Double ayoe;

	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
	@Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
	private LocalDateTime autualExperienceDate;

//	@Field(name = "cei_notice_period")
	private Integer noticePeriod;

//	@Field(name = "cei_designation")
	private String designation;

//	@Field(name = "cei_current_location")
	private String currentLocation;

//	@Field(name = "cei_preffered_location")
	private List<String> prefferedLocation;

//	@Field(name = "cei_job_description")
	private String jobDescription;

//	@Field(name = "cei_current_salary")
	private BigDecimal currentSalary;

//	@Field(name = "cei_expected_salary")
	private BigDecimal expectedSalary;

//	@Field(name = "cei_skill_set")
	@Convert(converter = StringListConverter.class)
	private List<String> skillSet;

//	@Field(name = "cei_reference_person_details", type = FieldType.Object)
	@Convert(converter = JsonToStringConverter.class)
	private Object referencePersonDetails;

//	public Object getStartDate() {
//		if (startDate  instanceof LocalDate) {
//			return startDate;
//		}
//		return startDate;
//	}
//
//	public void setStartDate(LocalDate startDate) {
//		this.startDate = startDate;
//	}
//
//	public LocalDate getEndDate() {
//		return endDate;
//	}
//
//	public void setEndDate(LocalDate endDate) {
//		this.endDate = endDate;
//	}
//
//	public LocalDateTime getAutualExperienceDate() {
//		return autualExperienceDate;
//	}
//
//	public void setAutualExperienceDate(LocalDateTime autualExperienceDate) {
//		this.autualExperienceDate = autualExperienceDate;
//	}
//	
}
