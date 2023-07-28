package com.techno.mpm.service.candidate;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.techno.mpm.pojo.candidate.CandidateDashboardPojo;
import com.techno.mpm.pojo.candidate.CandidateFilterPojo;
import com.techno.mpm.pojo.candidate.CandidatePersonalInfoPojo;
import com.techno.mpm.pojo.candidate.CandidateStatusHistoryPojo;
import com.techno.mpm.pojo.candidate.OnboardDetailsRequest;
import com.techno.mpm.pojo.candidate.OnboardDetailsResponse;
import com.techno.mpm.pojo.candidate.ResumePojo;

public interface CandidateInfoService {

	
	CandidatePersonalInfoPojo addCandidate(CandidatePersonalInfoPojo candidatePersonalInfoPojo, MultipartFile file);

	List<CandidatePersonalInfoPojo> addCandidates(List<CandidatePersonalInfoPojo> candidatePersonalInfoPojos);

	CandidatePersonalInfoPojo addResume(String candidateId, MultipartFile resume);

	CandidatePersonalInfoPojo getCandidate(String candidateId);

//	ResumePojo downloadResume(String candidateId, HttpServletResponse response);

	CandidateDashboardPojo getCandidates(CandidateFilterPojo candidateFilterPojo);

	CandidatePersonalInfoPojo deleteCandidate(String candidateId);

	String downloadCandidateInfo(MultipartFile file, String mailTo);

	String candidateSendLink(CandidatePersonalInfoPojo candidatePersonalInfoPojo);

	OnboardDetailsResponse candidateVarify(OnboardDetailsRequest onboardDetailsRequest);

	CandidateStatusHistoryPojo updateCandidateStatus(CandidateStatusHistoryPojo candidateStatusHistoryPojo);

	List<String> getClient();

}
