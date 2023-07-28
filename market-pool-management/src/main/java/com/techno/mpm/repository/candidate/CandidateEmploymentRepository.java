package com.techno.mpm.repository.candidate;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techno.mpm.entity.candidate.CandidateEmploymentInfo;

public interface CandidateEmploymentRepository extends JpaRepository<CandidateEmploymentInfo, String> {

	
	Optional<List<CandidateEmploymentInfo>> findByCandidatePersonalInfoIsDeleteFalse();

}
