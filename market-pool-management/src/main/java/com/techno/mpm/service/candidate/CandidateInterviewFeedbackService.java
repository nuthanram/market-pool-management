package com.techno.mpm.service.candidate;

import com.techno.mpm.pojo.candidate.CandidateInterviewFeedbackPojo;
import com.techno.mpm.pojo.candidate.FeedbackPojo;

public interface CandidateInterviewFeedbackService {

	CandidateInterviewFeedbackPojo saveClientFeedback(CandidateInterviewFeedbackPojo candidateInterviewFeedbackPojo);

	CandidateInterviewFeedbackPojo getClientFeedback(String feedbackId);

	FeedbackPojo saveCandidateFeedback(FeedbackPojo feedbackPojo);

	FeedbackPojo getCandidatedback(String feedbackId);
	
}
