package com.techno.mpm.service.candidate;

import static com.techno.mpm.appconstant.CommonConstants.MAIL_NOT_SEND;
import static com.techno.mpm.appconstant.CommonConstants.MAIL_SEND_SECCUSSFULLY;
import static com.techno.mpm.appconstant.ExceptionConstant.INVALID_CANDIDATE_ID;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techno.mpm.beancopy.BeanCopy;
import com.techno.mpm.document.candidate.CandidateDocument;
import com.techno.mpm.document.candidate.CandidateEmploymentDocument;
import com.techno.mpm.dto.mail.MailDto;
import com.techno.mpm.elastic.repository.candidate.DocumentRepository;
import com.techno.mpm.entity.candidate.CandidateEmploymentInfo;
import com.techno.mpm.entity.candidate.CandidatePersonalInfo;
import com.techno.mpm.entity.candidate.CandidateStatusHistory;
import com.techno.mpm.entity.candidate.ClientInterviewFeedback;
import com.techno.mpm.enumeration.CandidateEnum;
import com.techno.mpm.exception.DataNotFoundException;
import com.techno.mpm.pojo.candidate.CandiadateEmployentInfoPojo;
import com.techno.mpm.pojo.candidate.CandidateDashboardPojo;
import com.techno.mpm.pojo.candidate.CandidateFilterPojo;
import com.techno.mpm.pojo.candidate.CandidateInterviewFeedbackPojo;
import com.techno.mpm.pojo.candidate.CandidatePersonalInfoPojo;
import com.techno.mpm.pojo.candidate.CandidateStatusHistoryPojo;
import com.techno.mpm.pojo.candidate.FeedbackPojo;
import com.techno.mpm.pojo.candidate.OnboardDetailsRequest;
import com.techno.mpm.pojo.candidate.OnboardDetailsResponse;
import com.techno.mpm.repository.candidate.CandidateInterviewFeedbackRepository;
import com.techno.mpm.repository.candidate.CandidatePersonalInfoRepository;
import com.techno.mpm.service.mail.EmailService;
import com.techno.mpm.utils.DOCXTextExtractor;
import com.techno.mpm.utils.PDFTextExtractor;
import com.techno.mpm.utils.SSSUploadFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateInfoServiceImpl implements CandidateInfoService {

	private final EmailService emailService;
	private final SSSUploadFile uploadFile;
	private final DocumentRepository documentRepository;
	private final CandidatePersonalInfoRepository candidatePersonalInfoRepository;
	private final CandidateInterviewFeedbackRepository candidateInterviewFeedbackRepository;

	@Override
	public CandidatePersonalInfoPojo addCandidate(CandidatePersonalInfoPojo candidatePersonalInfoPojo,
			MultipartFile file) {
		try {
			if (candidatePersonalInfoPojo.getCandidateId() == null) {
				CandidatePersonalInfo candidatePersonalInfo = new CandidatePersonalInfo();
				BeanUtils.copyProperties(candidatePersonalInfoPojo, candidatePersonalInfo);
				List<CandidateEmploymentInfo> candidateEmploymentInfos = BeanCopy
						.copy(candidatePersonalInfoPojo.getCandidateEmploymentInfos(), CandidateEmploymentInfo.class);
				candidateEmploymentInfos.forEach(candidateEmploymentInfo -> candidateEmploymentInfo
						.setCandidatePersonalInfo(candidatePersonalInfo));
				candidatePersonalInfo.setCandidateEmploymentInfos(candidateEmploymentInfos);
				candidatePersonalInfo.setIsDelete(Boolean.FALSE);
				candidatePersonalInfo.setCandidateStatus(CandidateEnum.SOURCED);
				CandidateStatusHistory candidateStatusHistory = new CandidateStatusHistory();
				candidateStatusHistory.setDate(LocalDate.now());
				candidateStatusHistory.setCandidateEnum(CandidateEnum.SOURCED);
				candidatePersonalInfo.setCandidateStatusHistories(Arrays.asList(candidateStatusHistory));
				CandidateDocument candidateDocument = new CandidateDocument();
				BeanUtils.copyProperties(candidatePersonalInfo, candidateDocument);
				List<CandidateEmploymentDocument> candidateEmploymentDocuments = BeanCopy
						.copy(candidatePersonalInfo.getCandidateEmploymentInfos(), CandidateEmploymentDocument.class);
				candidateDocument.setCandidateEmploymentInfos(candidateEmploymentDocuments);
				if (file != null && file.getOriginalFilename() != null) {
					String[] split = file.getOriginalFilename().split("\\.");
					if ("pdf".equals(split[split.length - 1]))
						candidateDocument.setResumeContent(PDFTextExtractor.extractTextFromFile2(file));
					else
						candidateDocument.setResumeContent(DOCXTextExtractor.extractTextFromMultipartFile(file));
				}
				String resumePath = uploadFile.uploadFile(file);
				candidatePersonalInfo.setResumeUrl(resumePath);
				CandidatePersonalInfo personalInfo = candidatePersonalInfoRepository.save(candidatePersonalInfo);
				candidateDocument.setId(personalInfo.getCandidateId());
				candidateDocument.setCandidateId(personalInfo.getCandidateId());
				documentRepository.save(candidateDocument);
				BeanUtils.copyProperties(candidatePersonalInfo, candidatePersonalInfoPojo);

				return candidatePersonalInfoPojo;
			}

			return candidatePersonalInfoRepository
					.findByCandidateIdAndIsDeleteFalse(candidatePersonalInfoPojo.getCandidateId())
					.map(candidatePersonalInfo -> {
						Object candidateStatusHistories = candidatePersonalInfo.getCandidateStatusHistories();
						BeanUtils.copyProperties(candidatePersonalInfoPojo, candidatePersonalInfo, "resumeUrl");
						List<CandidateEmploymentInfo> candidateEmploymentInfos = BeanCopy.copy(
								candidatePersonalInfoPojo.getCandidateEmploymentInfos(), CandidateEmploymentInfo.class);
						candidateEmploymentInfos.forEach(candidateEmploymentInfo -> candidateEmploymentInfo
								.setCandidatePersonalInfo(candidatePersonalInfo));
						candidatePersonalInfo.setCandidateEmploymentInfos(candidateEmploymentInfos);
						candidatePersonalInfo.setCandidateStatus(
								candidatePersonalInfoPojo.getCandidateStatus() == null ? CandidateEnum.SOURCED
										: candidatePersonalInfoPojo.getCandidateStatus());
						candidatePersonalInfo.setIsDelete(Boolean.FALSE);
						CandidateDocument candidateDocument = documentRepository
								.findById(candidatePersonalInfoPojo.getCandidateId()).orElseThrow();
						candidateDocument.setCandidateStatusHistories(
								candidatePersonalInfoPojo.getCandidateStatus() == null ? CandidateEnum.SOURCED
										: candidatePersonalInfoPojo.getCandidateStatus());
						if (file != null) {

							String[] split = file.getOriginalFilename().split("\\.");
							if ("pdf".equals(split[1]))
								candidateDocument.setResumeContent(PDFTextExtractor.extractTextFromFile2(file));
							else
								candidateDocument
										.setResumeContent(DOCXTextExtractor.extractTextFromMultipartFile(file));
							if (candidatePersonalInfo.getResumeUrl() != null) {
								uploadFile.deleteS3Folder(candidatePersonalInfo.getResumeUrl());
							}
							String resumePath = uploadFile.uploadFile(file);
							candidatePersonalInfo.setResumeUrl(resumePath);
						}
						candidatePersonalInfo.setCandidateStatusHistories(candidateStatusHistories);
						BeanUtils.copyProperties(candidatePersonalInfo, candidateDocument);
						List<CandidateEmploymentDocument> candidateEmploymentDocument = BeanCopy
								.copy(candidateEmploymentInfos, CandidateEmploymentDocument.class);
						candidateDocument.setCandidateEmploymentInfos(candidateEmploymentDocument);
						candidateDocument.setId(candidateDocument.getCandidateId());
						documentRepository.save(candidateDocument);
						BeanUtils.copyProperties(candidatePersonalInfoRepository.save(candidatePersonalInfo),
								candidatePersonalInfoPojo);
						return candidatePersonalInfoPojo;
					}).orElseThrow(() -> new DataNotFoundException(INVALID_CANDIDATE_ID));
		} catch (Exception e) {
			e.printStackTrace();
			Throwable cause2 = e.getCause();
			throw new DataNotFoundException(
					cause2.getCause() == null ? cause2.getMessage() : cause2.getCause().getMessage(), e);
		}
	}

	@Override
	public List<CandidatePersonalInfoPojo> addCandidates(List<CandidatePersonalInfoPojo> candidatePersonalInfoPojos) {
		try {
			return BeanCopy.copyItr(documentRepository.saveAll(candidatePersonalInfoRepository
					.saveAll(candidatePersonalInfoPojos.stream().map(candidatePersonalInfoPojo -> {
						CandidatePersonalInfo candidatePersonalInfo = new CandidatePersonalInfo();
						BeanUtils.copyProperties(candidatePersonalInfoPojo, candidatePersonalInfo);
						List<CandidateEmploymentInfo> candidateEmploymentInfos = BeanCopy.copy(
								candidatePersonalInfoPojo.getCandidateEmploymentInfos(), CandidateEmploymentInfo.class);
						candidateEmploymentInfos.forEach(candidateEmploymentInfo -> candidateEmploymentInfo
								.setCandidatePersonalInfo(candidatePersonalInfo));
						candidatePersonalInfo.setCandidateEmploymentInfos(candidateEmploymentInfos);
						CandidateStatusHistory candidateStatusHistory = new CandidateStatusHistory();
						candidateStatusHistory.setDate(LocalDate.now());
						candidateStatusHistory.setCandidateEnum(CandidateEnum.SOURCED);
						candidatePersonalInfo.setCandidateStatusHistories(Arrays.asList(candidateStatusHistory));
						candidatePersonalInfo.setIsDelete(Boolean.FALSE);
						candidatePersonalInfo.setCandidateStatus(CandidateEnum.SOURCED);
						return candidatePersonalInfo;
					}).collect(Collectors.toList())).stream().map(candidatePersonalInfo -> {
						CandidateDocument candidateDocument = new CandidateDocument();
						BeanUtils.copyProperties(candidatePersonalInfo, candidateDocument);
						List<CandidateEmploymentDocument> candidateEmploymentInfos = BeanCopy.copy(
								candidatePersonalInfo.getCandidateEmploymentInfos(), CandidateEmploymentDocument.class);
						candidateDocument.setCandidateEmploymentInfos(candidateEmploymentInfos);
						candidateDocument.setId(candidatePersonalInfo.getCandidateId());
						return candidateDocument;
					}).collect(Collectors.toList())), CandidatePersonalInfoPojo.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataNotFoundException(e.getMessage(), e);
		}
	}

	@Transactional
	@Override
	public CandidatePersonalInfoPojo addResume(String candidateId, MultipartFile resume) {
		try {
			return candidatePersonalInfoRepository.findByCandidateIdAndIsDeleteFalse(candidateId)
					.map(candidatePersonalInfo -> {
						candidatePersonalInfo.setResumeUrl(uploadFile.uploadFile(resume));
						CandidatePersonalInfoPojo candidatePersonalInfoPojo = new CandidatePersonalInfoPojo();
						BeanUtils.copyProperties(candidatePersonalInfo, candidatePersonalInfoPojo);
						CandidateDocument candidateDocument = documentRepository.findByCandidateId(candidateId)
								.orElseThrow(() -> new DataNotFoundException(INVALID_CANDIDATE_ID));
						if (resume != null) {
							String[] split = resume.getOriginalFilename().split("\\.");
							if ("pdf".equals(split[split.length - 1]))
								candidateDocument.setResumeContent(PDFTextExtractor.extractTextFromFile2(resume));
							else
								candidateDocument
										.setResumeContent(DOCXTextExtractor.extractTextFromMultipartFile(resume));
						}
						documentRepository.save(candidateDocument);
						return candidatePersonalInfoPojo;
					}).orElseThrow(() -> new DataNotFoundException(INVALID_CANDIDATE_ID));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataNotFoundException(e.getMessage(), e);
		}
	}

	@Override
	public CandidatePersonalInfoPojo getCandidate(String candidateId) {
		try {
			return candidatePersonalInfoRepository.findByCandidateIdAndIsDeleteFalse(candidateId).map(candidate -> {
				CandidatePersonalInfoPojo candidatePersonalInfoPojo = new CandidatePersonalInfoPojo();
				BeanUtils.copyProperties(candidate, candidatePersonalInfoPojo);
				CandiadateEmployentInfoPojo candiadateEmployentInfoPojo = new CandiadateEmployentInfoPojo();
				BeanUtils.copyProperties(
						candidate.getCandidateEmploymentInfos().get(candidate.getCandidateEmploymentInfos().size() - 1),
						candiadateEmployentInfoPojo);
				candidatePersonalInfoPojo.setCandidateEmploymentInfos(List.of(candiadateEmployentInfoPojo));
				return candidatePersonalInfoPojo;
			}).orElseThrow(() -> new DataNotFoundException(INVALID_CANDIDATE_ID));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataNotFoundException(e.getMessage(), e);
		}
	}

	@Override
	public CandidateDashboardPojo getCandidates(CandidateFilterPojo candidateFilterPojo) {
		try {
			CandidateDashboardPojo candidateDetails = getCandidateDetails(candidateFilterPojo.getFromDate(),
					candidateFilterPojo.getToDate());
			return CandidateDashboardPojo.builder().available(candidateDetails.getAvailable())
					.deployed(candidateDetails.getDeployed()).notAvailable(candidateDetails.getNotAvailable())
					.selected(candidateDetails.getSelected()).totalCandidate(candidateDetails.getTotalCandidate())
					.candidatePersonalInfoPojos(candidatePersonalInfoRepository
							.findByIsDeleteFalseAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndCandidateStatusIn(
									candidateFilterPojo.getFromDate() == null ? LocalDateTime.now().minusDays(7)
											: candidateFilterPojo.getFromDate().atStartOfDay(),
									candidateFilterPojo.getToDate() == null ? LocalDateTime.now()
											: candidateFilterPojo.getToDate().atTime(23, 59, 59),
									candidateFilterPojo.getCandidateEnum() == null
											? Arrays.asList(CandidateEnum.values())
											: getCandidateStatus(candidateFilterPojo.getCandidateEnum()))
							.map(candidates -> candidates.stream().map(candidate -> {
								CandidatePersonalInfoPojo candidatePersonalInfoPojo = CandidatePersonalInfoPojo
										.builder()
										.candidateInterviewFeedbackPojos(candidate.getCandidateInterviewFeedbacks()
												.stream()
												.map(candidateInterviewFeedback -> CandidateInterviewFeedbackPojo
														.builder()
														.companyName(candidateInterviewFeedback.getCompanyName())
														.interviewRating(
																candidateInterviewFeedback.getInterviewRating())
														.interviewStatus(
																candidateInterviewFeedback.getInterviewStatus())
														.feedback(candidateInterviewFeedback.getFeedback()).build())
												.collect(Collectors.toList()))
										.technologies(candidate.getCandidateEmploymentInfos().stream()
												.flatMap(candidateEmployentInfo -> candidateEmployentInfo.getSkillSet()
														.stream())
												.collect(Collectors.toSet()))
										.feedbackPojos(
												BeanCopy.copy(candidate.getFeedbackHistories(), FeedbackPojo.class))
										.candidateEmploymentInfos(BeanCopy.copy(candidate.getCandidateEmploymentInfos(),
												CandiadateEmployentInfoPojo.class))
										.build();
								BeanUtils.copyProperties(candidate, candidatePersonalInfoPojo);
								return candidatePersonalInfoPojo;
							}).collect(Collectors.toList())).orElseGet(ArrayList::new))
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataNotFoundException(e.getMessage(), e);
		}
	}

	private List<CandidateEnum> getCandidateStatus(CandidateEnum candidateStatus) {
		if (CandidateEnum.ALL.equals(candidateStatus))
			return Arrays.asList(CandidateEnum.values());
		else if (CandidateEnum.DEPLOYED.equals(candidateStatus))
			return Arrays.asList(candidateStatus);
		else if (CandidateEnum.NOT_AVAILABLE.equals(candidateStatus))
			return Arrays.asList(CandidateEnum.BLOCKED_BY_TY, CandidateEnum.SELECTED_FOR_OTHER_COMPANY,
					CandidateEnum.NOT_INTERESTED_FOR_TY, CandidateEnum.OFFER_DECLINED);
		else if (CandidateEnum.AVAILABLE.equals(candidateStatus))
			return Arrays.asList(CandidateEnum.SOURCED, CandidateEnum.INTERVIEW_SCHEDULED,
					CandidateEnum.INTERVIEW_REJECT);
		else if (CandidateEnum.SELECTED.equals(candidateStatus))
			return Arrays.asList(CandidateEnum.CLIENT_SELECT, CandidateEnum.EDF_SENT, CandidateEnum.OFFER_RELEASED,
					CandidateEnum.YET_TO_JOIN_TY, CandidateEnum.ONBOARDED_TO_TY);

		return Arrays.asList(CandidateEnum.values());
	}

	private CandidateDashboardPojo getCandidateDetails(LocalDate fromDate, LocalDate toDate) {
		try {
			List<CandidatePersonalInfo> candidatePersonalInfos = candidatePersonalInfoRepository
					.findByIsDeleteFalseAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(
							fromDate == null ? LocalDateTime.now().minusDays(7) : fromDate.atStartOfDay(),
							toDate == null ? LocalDateTime.now() : toDate.atTime(23, 59, 59))
					.orElseGet(ArrayList::new);
			int totalCandidate = candidatePersonalInfos.size();
			return candidatePersonalInfos.stream().collect(Collectors.collectingAndThen(
					Collectors.groupingBy(CandidatePersonalInfo::getCandidateStatus, Collectors.counting()),
					mapOfCandidateEnum -> CandidateDashboardPojo.builder()
							.deployed(Optional.ofNullable(mapOfCandidateEnum.get(CandidateEnum.DEPLOYED)).orElse(0l))
							.notAvailable(
									Optional.ofNullable(mapOfCandidateEnum.get(CandidateEnum.BLOCKED_BY_TY)).orElse(0l)
											+ Optional.ofNullable(
													mapOfCandidateEnum.get(CandidateEnum.SELECTED_FOR_OTHER_COMPANY))
													.orElse(0l)
											+ Optional
													.ofNullable(
															mapOfCandidateEnum.get(CandidateEnum.NOT_INTERESTED_FOR_TY))
													.orElse(0l)
											+ Optional.ofNullable(mapOfCandidateEnum.get(CandidateEnum.OFFER_DECLINED))
													.orElse(0l))
							.available(Optional.ofNullable(mapOfCandidateEnum.get(CandidateEnum.SOURCED)).orElse(0l)
									+ Optional.ofNullable(mapOfCandidateEnum.get(CandidateEnum.INTERVIEW_SCHEDULED))
											.orElse(0l)
									+ Optional.ofNullable(mapOfCandidateEnum.get(CandidateEnum.INTERVIEW_REJECT))
											.orElse(0l))
							.selected(
									Optional.ofNullable(mapOfCandidateEnum.get(CandidateEnum.CLIENT_SELECT)).orElse(0l)
											+ Optional.ofNullable(mapOfCandidateEnum.get(CandidateEnum.ONBOARDED_TO_TY))
													.orElse(0l)
											+ Optional.ofNullable(mapOfCandidateEnum.get(CandidateEnum.OFFER_RELEASED))
													.orElse(0l)
											+ Optional.ofNullable(mapOfCandidateEnum.get(CandidateEnum.YET_TO_JOIN_TY))
													.orElse(0l))
//							+ Optional.ofNullable(mapOfCandidateEnum.get(CandidateEnum.DEPLOYED))
//									.orElse(0l)
							.totalCandidate((long) totalCandidate).build()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataNotFoundException(e.getMessage(), e);
		}
	}

//	@Override
//	public ResumePojo downloadResume(String candidateId, HttpServletResponse response) {
//		try {
//			CandidatePersonalInfo candidate = candidatePersonalInfoRepository
//					.findByCandidateIdAndIsDeleteFalse(candidateId)
//					.orElseThrow(() -> new DataNotFoundException(INVALID_CANDIDATE_ID));
//			String filePath = candidate.getResumeUrl();
//			byte[] resumeDownload = Files.readAllBytes(new File(filePath).toPath());
//			return ResumePojo.builder().fileExtension(filePath.substring(filePath.lastIndexOf(".") + 1))
//					.fileName(candidate.getCandidateName()).resumeDownload(resumeDownload).filePath(filePath).build();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new DataNotFoundException(e.getMessage(), e);
//		}
//	}

	@Override
	public String downloadCandidateInfo(MultipartFile file, String mailTo) {
		try {
			Integer mail = emailService.sendMailWithAttachment(
					new ObjectMapper().writeValueAsString(MailDto.builder().subject("List of Candidate details")
							.body("PFA Candidate Details \\r\\n\\r\\n Thanks and Regards \\r\\n Team TechnoElevate")
							.to(mailTo).build()),
					file);
			if (mail == 200)
				return MAIL_SEND_SECCUSSFULLY;
			else
				return MAIL_NOT_SEND;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataNotFoundException(e.getMessage(), e);
		}
	}

	@Transactional
	@Override
	public CandidatePersonalInfoPojo deleteCandidate(String candidateId) {
		try {
			return candidatePersonalInfoRepository.findByCandidateIdAndIsDeleteFalse(candidateId).map(candidate -> {
				candidate.setIsDelete(Boolean.TRUE);
				CandidatePersonalInfoPojo candidatePersonalInfoPojo = new CandidatePersonalInfoPojo();
				BeanUtils.copyProperties(candidate, candidatePersonalInfoPojo);
				documentRepository.deleteById(candidateId);
				return candidatePersonalInfoPojo;
			}).orElseThrow(() -> new DataNotFoundException(INVALID_CANDIDATE_ID));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataNotFoundException(e.getMessage(), e);
		}
	}

	@Override
	public String candidateSendLink(CandidatePersonalInfoPojo candidatePersonalInfoPojo) {
		CandidatePersonalInfo candidatePersonalInfo = candidatePersonalInfoRepository
				.findByCandidateIdAndIsDeleteFalse(candidatePersonalInfoPojo.getCandidateId())
				.orElseThrow(() -> new DataNotFoundException("Candidate Not Found"));
		return emailService.sendMailWithLink(MailDto.builder().to(candidatePersonalInfo.getEmailId())
				.subject("Candidate Registration Form - Please Fill Out")
				.body(getBoby(candidatePersonalInfo.getCandidateName(), candidatePersonalInfoPojo.getUrl()))
				.build()) == 200 ? "Send Link Successfully To The Selected Candidate" : "Something Went Wrong";
	}

	private String getBoby(String name, String link) {
		return "<!DOCTYPE html>\r\n" + "<html lang=\"en\">\r\n" + "  <head>\r\n" + "    <meta charset=\"UTF-8\" />\r\n"
				+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\r\n"
				+ "    <title>Candidate Send Link</title>\r\n" + "  </head>\r\n" + "  <body>\r\n"
				+ "    <div style=\"height: 100vh; margin-left: 50px; margin-top: 25px\">\r\n"
				+ "      <h1>Fill the Form</h1>\r\n" + "      <p>Dear " + name + ",</p>\r\n"
				+ "      <p>Please click the link below to fill out the form:</p>\r\n" + "      <a href=" + link
				+ " target=\"_blank\">" + link + "</a>\r\n" + "      <br />\r\n" + "      <br />\r\n"
				+ "      <p>Thanks and Regards,</p>\r\n" + "      <p>Technoelevate private ltd.</p>\r\n"
				+ "    </div>\r\n" + "  </body>\r\n" + "</html>\r\n" + "";
	}

	@Override
	public OnboardDetailsResponse candidateVarify(OnboardDetailsRequest onboardDetailsRequest) {
		return candidatePersonalInfoRepository
				.findByEmailIdAndMobileNumberAndIsDeleteFalse(onboardDetailsRequest.getEmail(),
						onboardDetailsRequest.getMobileNumber())
				.map(can -> OnboardDetailsResponse.builder().message("Candidate Fetch Successfully").isAvailable(true)
						.build())
				.orElseGet(() -> OnboardDetailsResponse.builder().message("Candidate Not Found").isAvailable(false)
						.build());
	}

	@Transactional
	@Override
	public CandidateStatusHistoryPojo updateCandidateStatus(CandidateStatusHistoryPojo candidateStatusHistoryPojo) {
		try {
			return candidatePersonalInfoRepository
					.findByCandidateIdAndIsDeleteFalse(candidateStatusHistoryPojo.getCandidateId())
					.map(candidateInfo -> {
						List<CandidateStatusHistory> candidateStatusHistories = BeanCopy.objectProperties(
								candidateInfo.getCandidateStatusHistories(),
								new TypeReference<List<CandidateStatusHistory>>() {
								});

						candidateStatusHistories = candidateStatusHistories == null ? new ArrayList<>()
								: candidateStatusHistories;
						if (!candidateStatusHistories.isEmpty()) {
							CandidateStatusHistory candidateStatusHistory = candidateStatusHistories
									.get(candidateStatusHistories.size() - 1);
							if (!candidateStatusHistory.getCandidateEnum()
									.equals(candidateStatusHistoryPojo.getCandidateEnum())) {
								CandidateStatusHistory candidateHistory = new CandidateStatusHistory();
								BeanUtils.copyProperties(candidateStatusHistoryPojo, candidateHistory);
								candidateStatusHistories.add(candidateHistory);
								candidateInfo.setCandidateStatusHistories(candidateStatusHistories);
								candidateInfo.setCandidateStatus(candidateStatusHistoryPojo.getCandidateEnum());
								CandidateDocument candidateDocument = documentRepository
										.findById(candidateInfo.getCandidateId())
										.orElseThrow(() -> new DataNotFoundException(
												"Candidate Not Fount In Elastic Search Document"));
								candidateDocument.setCandidateStatus(candidateStatusHistoryPojo.getCandidateEnum());
								documentRepository.save(candidateDocument);
							}
						}
						return candidateStatusHistoryPojo;
					}).orElseThrow(() -> new DataNotFoundException(INVALID_CANDIDATE_ID));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataNotFoundException(e.getMessage(), e);
		}
	}

	@Override
	public List<String> getClient() {
		try {
			return candidateInterviewFeedbackRepository.findAll().stream().map(ClientInterviewFeedback::getCompanyName)
					.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataNotFoundException(e.getMessage(), e);
		}

	}
}
