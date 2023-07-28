package com.techno.mpm.repository.candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techno.mpm.entity.candidate.CandidatePersonalInfo;
import com.techno.mpm.enumeration.CandidateEnum;

public interface CandidatePersonalInfoRepository extends JpaRepository<CandidatePersonalInfo, String> {

	
	Optional<CandidatePersonalInfo> findByCandidateIdAndIsDeleteFalse(String candidate);
	
	Optional<CandidatePersonalInfo> findByEmailIdAndIsDeleteFalse(String emailId);

	Optional<List<CandidatePersonalInfo>> findByIsDeleteFalseAndCandidateStatusIn(List<CandidateEnum> candidateEnum);

	Optional<List<CandidatePersonalInfo>> findByIsDeleteFalseAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndCandidateStatusIn(
			LocalDateTime fromDate, LocalDateTime toDate, List<CandidateEnum> candidateEnum);

	Optional<List<CandidatePersonalInfo>> findByIsDeleteFalseAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(
			LocalDateTime fromDate, LocalDateTime toDate);

	Optional<List<CandidatePersonalInfo>> findByIsDeleteFalse();

	Optional<CandidatePersonalInfo> findByEmailIdAndMobileNumberAndIsDeleteFalse(String emailId, String mobileNumber);
	
	Optional<List<CandidatePersonalInfo>> findByIsDeleteFalseAndCandidateEmploymentInfosSkillSetIsNotNull();

}
