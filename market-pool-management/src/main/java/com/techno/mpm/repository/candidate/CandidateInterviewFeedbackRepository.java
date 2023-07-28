package com.techno.mpm.repository.candidate;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techno.mpm.entity.candidate.ClientInterviewFeedback;

public interface CandidateInterviewFeedbackRepository extends JpaRepository<ClientInterviewFeedback, String> {

	
	Optional<List<ClientInterviewFeedback>> findByIsDeleteFalseAndCandidatePersonalInfoCandidateIdAndCandidatePersonalInfoIsDeleteFalse(
			String candidatePersonalInfoId);

	Optional<ClientInterviewFeedback> findByCandidateFeedbackIdAndIsDeleteFalse(String feedbackId);

}
