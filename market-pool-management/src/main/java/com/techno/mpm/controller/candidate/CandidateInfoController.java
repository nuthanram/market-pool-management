package com.techno.mpm.controller.candidate;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techno.mpm.beancopy.BeanCopy;
import com.techno.mpm.pojo.candidate.CandidateFilterPojo;
import com.techno.mpm.pojo.candidate.CandidatePersonalInfoPojo;
import com.techno.mpm.pojo.candidate.CandidateStatusHistoryPojo;
import com.techno.mpm.pojo.candidate.OnboardDetailsRequest;
import com.techno.mpm.pojo.candidate.OnboardDetailsResponse;
import com.techno.mpm.response.SuccessResponse;
import com.techno.mpm.service.candidate.CandidateInfoService;

import lombok.RequiredArgsConstructor;

@RestController("candidateInfoController")
@RequestMapping("api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CandidateInfoController {

	private final CandidateInfoService candidateInfoService;

	@PostMapping("candidate")
	public ResponseEntity<SuccessResponse> addCandidate(
			@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestPart(value = "data") String data) {
		CandidatePersonalInfoPojo candidatePersonalInfoPojo = BeanCopy.jsonProperties(data,
				CandidatePersonalInfoPojo.class);
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().isError(Boolean.FALSE)
				.message(candidatePersonalInfoPojo.getCandidateId() == null ? "Candidate Registered Successfully"
						: "Candidate Updated Successfully")
				.data(candidateInfoService.addCandidate(candidatePersonalInfoPojo, file)).build());
	}

	@PostMapping("candidates")
	public ResponseEntity<SuccessResponse> addCandidates(
			@RequestBody List<CandidatePersonalInfoPojo> candidatePersonalInfoPojos) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().isError(Boolean.FALSE).message("Candidates Saved Successfully")
						.data(candidateInfoService.addCandidates(candidatePersonalInfoPojos)).build());
	}

	@PutMapping("candidate/{candidateId}")
	public ResponseEntity<SuccessResponse> addResume(@PathVariable String candidateId,
			@RequestPart MultipartFile resume) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().isError(Boolean.FALSE).message("Candidate Updated Successfully")
						.data(candidateInfoService.addResume(candidateId, resume)).build());
	}

	@GetMapping("candidate/{candidateId}")
	public ResponseEntity<SuccessResponse> getCandidate(@PathVariable String candidateId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().isError(Boolean.FALSE).message("Candidate fetched Successfully")
						.data(candidateInfoService.getCandidate(candidateId)).build());
	}

//	@GetMapping("candidate/resume/{candidateId}")
//	public void downloadResume(@PathVariable String candidateId, HttpServletResponse response) {
//		ResumePojo resume = candidateInfoService.downloadResume(candidateId, response);
//		response.setContentType("pdf".equals(resume.getFileExtension()) ? "application/pdf"
//				: "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
//		response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
//				"inline;filename=" + resume.getFileName() + "." + resume.getFileExtension());
//		try (InputStream inputStream = new FileInputStream(resume.getFilePath());
//				OutputStream outputStream = response.getOutputStream()) {
//
//			byte[] buffer = new byte[1024];
//			int bytesRead;
//			while ((bytesRead = inputStream.read(buffer)) != -1) {
//				outputStream.write(buffer, 0, bytesRead);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new DataNotFoundException(e.getMessage(), e);
//		}
//
//	}

	@PostMapping("candidate-verify")
	public ResponseEntity<OnboardDetailsResponse> candidateVarify(
			@RequestBody OnboardDetailsRequest onboardDetailsRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(candidateInfoService.candidateVarify(onboardDetailsRequest));
	}

	@PostMapping("get-candidates")
	public ResponseEntity<SuccessResponse> getCandidates(@RequestBody CandidateFilterPojo candidateFilterPojo) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().isError(Boolean.FALSE).message("Candidates fetched Successfully")
						.data(candidateInfoService.getCandidates(candidateFilterPojo)).build());
	}

	@DeleteMapping("/candidate/{candidateId}")
	public ResponseEntity<SuccessResponse> deleteCandidate(@PathVariable String candidateId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().isError(Boolean.FALSE).message("Candidates deleted Successfully")
						.data(candidateInfoService.deleteCandidate(candidateId)).build());
	}

	@PostMapping("/download-candidateInfo")
	public ResponseEntity<SuccessResponse> downloadCandidateInfo(@RequestPart(value = "file") MultipartFile file,
			@RequestPart(value = "mailTo") String mailTo) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().isError(Boolean.FALSE)
						.message("Candidate Info details send Successfully")
						.data(candidateInfoService.downloadCandidateInfo(file, mailTo)).build());
	}

	@PostMapping("candidate/send-link")
	public ResponseEntity<SuccessResponse> candidateSendLink(
			@RequestBody CandidatePersonalInfoPojo candidatePersonalInfoPojo) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().isError(Boolean.FALSE).message("Candidate Send Link Successfully")
						.data(candidateInfoService.candidateSendLink(candidatePersonalInfoPojo)).build());
	}

	@PutMapping("candidate/status")
	public ResponseEntity<SuccessResponse> updateCandidateStatus(
			@RequestBody CandidateStatusHistoryPojo candidateStatusHistory) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().isError(Boolean.FALSE).message("Candidate status updated Successfully")
						.data(candidateInfoService.updateCandidateStatus(candidateStatusHistory)).build());
	}

	@GetMapping("client")
	public ResponseEntity<SuccessResponse> getClient() {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().isError(Boolean.FALSE)
				.message("Fetch All Client").data(candidateInfoService.getClient()).build());
	}

//	@GetMapping("resume/download")
//	public ResponseEntity<Resource> download(@RequestParam(value = "path") String path) {
//		byte[] s3File = sssUploadFile.getS3File(path);
//		ByteArrayResource resource = null;
//		if (s3File != null) {
//			resource = new ByteArrayResource(s3File);
//		}
//		return ResponseEntity.ok().header("Content-type", "application/octet-stream")
//				.header("Content-disposition",
//						"attachment; filename=\""
//								+ path.replace("https://technoelevate-test.s3.ap-south-1.amazonaws.com/", "") + "\"")
//				.body(resource);
//
//	}

}
