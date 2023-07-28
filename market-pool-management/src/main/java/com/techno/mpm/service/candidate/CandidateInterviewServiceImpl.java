package com.techno.mpm.service.candidate;

import static com.techno.mpm.appconstant.ExceptionConstant.INVALID_CANDIDATE_ID;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.techno.mpm.beancopy.BeanCopy;
import com.techno.mpm.entity.candidate.CandidateStatusHistory;
import com.techno.mpm.entity.candidate.ClientInterviewFeedback;
import com.techno.mpm.entity.candidate.FeedbackHistory;
import com.techno.mpm.enumeration.CandidateEnum;
import com.techno.mpm.exception.DataNotFoundException;
import com.techno.mpm.pojo.candidate.CandidateInterviewFeedbackPojo;
import com.techno.mpm.pojo.candidate.FeedbackPojo;
import com.techno.mpm.repository.candidate.CandidateInterviewFeedbackRepository;
import com.techno.mpm.repository.candidate.CandidatePersonalInfoRepository;
import com.techno.mpm.repository.candidate.FeedbackHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateInterviewServiceImpl implements CandidateInterviewFeedbackService {

	private final FeedbackHistoryRepository feedbackHistoryRepository;
	private final CandidatePersonalInfoRepository candidatePersonalInfoRepository;
	private final CandidateInterviewFeedbackRepository candidateInterviewFeedbackRepository;

	@Transactional
	@Override
	public CandidateInterviewFeedbackPojo saveClientFeedback(
			CandidateInterviewFeedbackPojo candidateInterviewFeedbackPojo) {
		try {

			return candidatePersonalInfoRepository
					.findByCandidateIdAndIsDeleteFalse(candidateInterviewFeedbackPojo.getCandidatePersonalInfoId())
					.map(candidatePersonalInfo -> {
						List<CandidateStatusHistory> candidateStatusHistories = BeanCopy.objectProperties(
								candidatePersonalInfo.getCandidateStatusHistories(),
								new TypeReference<List<CandidateStatusHistory>>() {
								});
						CandidateStatusHistory candidateStatusHistory = BeanCopy
								.objectProperties(candidateInterviewFeedbackPojo, CandidateStatusHistory.class);
						candidateStatusHistory.setDate(candidateInterviewFeedbackPojo.getInterviewDate().toLocalDate());
						candidateStatusHistory.setCandidateEnum(CandidateEnum
										.valueOf(candidateInterviewFeedbackPojo.getInterviewStatus().name()));
						candidateStatusHistory.setType("Interview");
						candidateStatusHistories.add(candidateStatusHistory);
						candidatePersonalInfo.setCandidateStatusHistories(candidateStatusHistories);
						return Optional.ofNullable(candidateInterviewFeedbackPojo.getCandidateFeedbackId())
								.map(feedbackId -> {
									List<ClientInterviewFeedback> candidateInterviewFeedbacksUpdated = candidatePersonalInfo
											.getCandidateInterviewFeedbacks().stream()
											.map(candidateInterviewFeedback -> {
												if (feedbackId
														.equals(candidateInterviewFeedback.getCandidateFeedbackId())) {
													BeanUtils.copyProperties(candidateInterviewFeedbackPojo,
															candidateInterviewFeedback);
													return candidateInterviewFeedback;
												} else
													return candidateInterviewFeedback;
											}).collect(Collectors.toList());
									candidatePersonalInfo
											.setCandidateInterviewFeedbacks(candidateInterviewFeedbacksUpdated);
									candidatePersonalInfo.setIsDelete(Boolean.FALSE);
									BeanUtils.copyProperties(candidatePersonalInfo, candidateInterviewFeedbackPojo);
									return candidateInterviewFeedbackPojo;
								}).orElseGet(() -> {
									ClientInterviewFeedback candidateInterviewFeedback = new ClientInterviewFeedback();
									BeanUtils.copyProperties(candidateInterviewFeedbackPojo,
											candidateInterviewFeedback);
									List<ClientInterviewFeedback> candidateInterviewFeedbacks = candidatePersonalInfo
											.getCandidateInterviewFeedbacks();
									candidateInterviewFeedback.setIsDelete(Boolean.FALSE);
									candidateInterviewFeedback.setCandidatePersonalInfo(candidatePersonalInfo);
									candidateInterviewFeedbacks.add(candidateInterviewFeedback);
									candidateInterviewFeedbackRepository.save(candidateInterviewFeedback);
									BeanUtils.copyProperties(candidateInterviewFeedback,
											candidateInterviewFeedbackPojo);
									return candidateInterviewFeedbackPojo;
								});
					}).orElseThrow(() -> new DataNotFoundException(INVALID_CANDIDATE_ID));
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new DataNotFoundException(exception.getMessage(), exception);
		}
	}

	@Override
	public CandidateInterviewFeedbackPojo getClientFeedback(String feedbackId) {
		try {

			return candidateInterviewFeedbackRepository.findByCandidateFeedbackIdAndIsDeleteFalse(feedbackId)
					.map(canIntFeed -> {
						CandidateInterviewFeedbackPojo candidateInterviewFeedbackPojo = new CandidateInterviewFeedbackPojo();
						BeanUtils.copyProperties(canIntFeed, candidateInterviewFeedbackPojo);
						return candidateInterviewFeedbackPojo;
					}).orElseGet(CandidateInterviewFeedbackPojo::new);
		} catch (Exception exception) {
			throw new DataNotFoundException(exception.getMessage(), exception);
		}
	}

	@Transactional
	@Override
	public FeedbackPojo saveCandidateFeedback(FeedbackPojo feedbackPojo) {
		try {
			return candidatePersonalInfoRepository.findByCandidateIdAndIsDeleteFalse(feedbackPojo.getCandidateId())
					.map(candidateInfo -> {
						FeedbackHistory feedbackHistory = FeedbackHistory.builder()
								.candidateStatus(feedbackPojo.getCandidateStatus()).reason(feedbackPojo.getReason())
								.candidatePersonalInfo(candidateInfo).build();
						candidateInfo.getFeedbackHistories().add(feedbackHistory);
						candidateInfo.setCandidateStatus(feedbackPojo.getCandidateStatus());
						feedbackHistoryRepository.save(feedbackHistory);
						BeanUtils.copyProperties(feedbackHistory, feedbackPojo);
						return feedbackPojo;
					}).orElseThrow(() -> new DataNotFoundException(INVALID_CANDIDATE_ID));
		} catch (Exception exception) {
			throw new DataNotFoundException(exception.getMessage(), exception);
		}
	}

	@Override
	public FeedbackPojo getCandidatedback(String feedbackId) {
		try {
			return feedbackHistoryRepository.findById(feedbackId).map(candidateFeedback -> {
				FeedbackPojo feedbackPojo = new FeedbackPojo();
				BeanUtils.copyProperties(candidateFeedback, feedbackPojo);
				return feedbackPojo;
			}).orElseGet(FeedbackPojo::new);
		} catch (Exception exception) {
			throw new DataNotFoundException(exception.getMessage(), exception);
		}
	}

}
