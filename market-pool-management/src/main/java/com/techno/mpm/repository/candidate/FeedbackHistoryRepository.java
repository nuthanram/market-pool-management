package com.techno.mpm.repository.candidate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techno.mpm.entity.candidate.FeedbackHistory;

public interface FeedbackHistoryRepository extends JpaRepository<FeedbackHistory, String> {

	
}
