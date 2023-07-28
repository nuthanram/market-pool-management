package com.techno.mpm.service.mail;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techno.mpm.beancopy.BeanCopy;
import com.techno.mpm.dto.mail.MailDto;
import com.techno.mpm.exception.mail.MessagingException;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String from;

	
	public Integer sendMail(MailDto mailDto) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setSubject(mailDto.getSubject());
			helper.setText(mailDto.getBody());
			helper.setTo(mailDto.getTo());
			javaMailSender.send(message);
			return HttpStatus.OK.value();
		} catch (Exception e) {
			e.printStackTrace();
			throw new MessagingException(e.getMessage());
		}

	}

	public Integer sendMailWithLink(MailDto mailDto) {
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
			helper.setTo(mailDto.getTo());
			helper.setFrom(from);
			helper.setSubject(mailDto.getSubject());
			helper.setText(mailDto.getBody(), true);
			javaMailSender.send(message);
			return HttpStatus.OK.value();
		} catch (javax.mail.MessagingException e) {
			e.printStackTrace();
			throw new MessagingException(e.getMessage());
		}

	}

	public Integer sendMailWithAttachment(String mailDto, MultipartFile multipartFile) {
		try {
			MailDto mail = BeanCopy.jsonProperties(mailDto, MailDto.class);
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setSubject(mail.getSubject());
			helper.setText(mail.getBody());
			helper.setTo(mail.getTo());
			helper.addAttachment("" + multipartFile.getOriginalFilename(), multipartFile);
			javaMailSender.send(message);
			return HttpStatus.OK.value();
		} catch (javax.mail.MessagingException e) {
			e.printStackTrace();
			throw new MessagingException(e.getMessage());
		}

	}

	public Integer sendMailWithMultipleUser(MailDto mailDto) {
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
			helper.setFrom(from);
			helper.setSubject(mailDto.getSubject());
			helper.setText(mailDto.getBody(), true);
			helper.setTo(mailDto.getTos().toArray(String[]::new));
			javaMailSender.send(message);
			return HttpStatus.OK.value();
		} catch (javax.mail.MessagingException e) {
			e.printStackTrace();
			throw new MessagingException(e.getMessage());
		}
	}

}