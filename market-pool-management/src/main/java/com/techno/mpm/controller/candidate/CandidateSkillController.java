package com.techno.mpm.controller.candidate;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController("candidateSkillController")
@RequestMapping("api/v1/skills")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CandidateSkillController {

}
