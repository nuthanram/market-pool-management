package com.techno.mpm.schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techno.mpm.entity.candidate.CandidatePersonalInfo;
import com.techno.mpm.repository.candidate.CandidatePersonalInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduleTask {

	private final CandidatePersonalInfoRepository candidatePersonalInfoRepository;

	@Transactional
	@Scheduled(cron = "@daily", zone = "Asia/Kolkata")
	public void scheduleTasked() {
		List<CandidatePersonalInfo> collect = candidatePersonalInfoRepository.findAll().stream().map(exp -> {
			if (!exp.getCandidateEmploymentInfos().isEmpty()) {
				exp.setCandidateEmploymentInfos(exp.getCandidateEmploymentInfos().stream()
						.filter(autualExperienceDate -> autualExperienceDate.getAutualExperienceDate() != null)
						.map(employee -> {
							LocalDateTime autualExperienceDate = employee.getAutualExperienceDate();
							LocalDateTime localDateTime = LocalDateTime.now();
							LocalDateTime plusMonths = autualExperienceDate.plusMonths(1);
							if (plusMonths.isBefore(localDateTime) || plusMonths.equals(localDateTime)) {
								employee.setAyoe(employee.getAyoe() + 0.83);
								employee.setAutualExperienceDate(LocalDateTime.now());
							}
							return employee;
						}).collect(Collectors.toList()));
			}
			return exp;
		}).collect(Collectors.toList());
		log.info("Candidate Employment Info Update With Size {}",collect.size());
	}
}
