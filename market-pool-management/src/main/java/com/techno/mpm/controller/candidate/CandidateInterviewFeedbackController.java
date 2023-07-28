package com.techno.mpm.controller.candidate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techno.mpm.pojo.candidate.CandidateInterviewFeedbackPojo;
import com.techno.mpm.pojo.candidate.FeedbackPojo;
import com.techno.mpm.response.SuccessResponse;
import com.techno.mpm.service.candidate.CandidateInterviewFeedbackService;

import lombok.RequiredArgsConstructor;

@RestController("candidateInterviewFeedbackController")
@RequestMapping("api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CandidateInterviewFeedbackController {

	private final CandidateInterviewFeedbackService candidateInterviewFeedbackService;

	@PostMapping("/client/feedback")
	public ResponseEntity<SuccessResponse> saveClientFeedback(
			@RequestBody CandidateInterviewFeedbackPojo candidateInterviewFeedbackPojo) {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().isError(Boolean.FALSE)
				.message(candidateInterviewFeedbackPojo.getCandidateFeedbackId() == null ? "Candidate Feedback Added"
						: "Candidate Feedback Updated")
				.data(candidateInterviewFeedbackService.saveClientFeedback(candidateInterviewFeedbackPojo)).build());
	}
	

	@GetMapping("/client/feedback/{feedbackId}")
	public ResponseEntity<SuccessResponse> getClientFeedback(@PathVariable String feedbackId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().isError(Boolean.FALSE)
						.message("Candidate Feedback fetched Successfully")
						.data(candidateInterviewFeedbackService.getClientFeedback(feedbackId)).build());
	}
	
	@PostMapping("/candidate/feedback")
	public ResponseEntity<SuccessResponse> saveCandidateFeedback(
			@RequestBody FeedbackPojo feedbackPojo) {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().isError(Boolean.FALSE)
				.message("Candidate Feedback added")
				.data(candidateInterviewFeedbackService.saveCandidateFeedback(feedbackPojo)).build());
	}
	
	@GetMapping("/candidate/feedback/{feedbackId}")
	public ResponseEntity<SuccessResponse> getCandidateFeedback(@PathVariable String feedbackId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().isError(Boolean.FALSE)
						.message("Candidate Feedback fetched Successfully")
						.data(candidateInterviewFeedbackService.getCandidatedback(feedbackId)).build());
	}
}
