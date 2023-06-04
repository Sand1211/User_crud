package com.core.myapp.service;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.core.myapp.common.Utils;
import com.core.myapp.controller.vo.UserVO;
import com.core.myapp.repo.UserRepository;
import com.core.myapp.repo.entity.Token;
import com.core.myapp.repo.entity.UserDetail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

	private static final Logger LOGGER = LogManager.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private TemplateEngine engine;

	@Autowired
	private UserRepository repository;

	public EmailService(JavaMailSender javaMailSender, TemplateEngine engine) {
		this.javaMailSender = javaMailSender;
		this.engine = engine;
	}

	public boolean sendMail(UserVO vo) {
		LOGGER.info("Inside sendEmail()", vo);
		boolean retValue;
		try {
			Context context = new Context();
			context.setVariable("vo", vo);
			getPasswordAndFullName(vo);
			String process = engine.process("email/welcome.html", context);
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setSubject("Welcome to My Application");
			helper.setText(process, true);
			helper.setTo(vo.getEmail());
			javaMailSender.send(message);
			retValue = true;
			LOGGER.info("Email sent successfully", message);
		} catch (Exception e) {
			LOGGER.error("Failed to send email", "e.getStackTrace()");
			retValue = false;
		}
		return retValue;
	}

	public void sendVerificationMail(String url, UserDetail user)
			throws MessagingException, UnsupportedEncodingException {

		LOGGER.info("Inside sendEmail()");
		String mailContent = "<p> Hi, " + user.getFirstName() + " " + user.getLastName() + ", </p>"
				+ "<p>Thank you for registering with us," + ""
				+ "Please, follow the link below to complete your registration.</p>" + "<a href=\"" + url
				+ "\">Verify your email to activate your account</a>"
				+ "<p> Thank you <br> Users Registration Portal Service";

		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setSubject("Email Verification");
			helper.setFrom("javalearning1189@gmail.com", "User Registration Portal Service");
			helper.setText(mailContent, true);
			helper.setTo(user.getEmail());
			javaMailSender.send(message);
			LOGGER.info("Email sent successfully", message);
		} catch (Exception e) {
			LOGGER.error("Failed to send email{}", e.getMessage());
		}

	}

	private void getPasswordAndFullName(UserVO vo) {
		UserDetail findByEmail = repository.findByEmail(vo.getEmail());
		String fullname = findByEmail.getFirstName() + " " + findByEmail.getLastName();
		vo.setPassword(findByEmail.getPassword());
		vo.setFullName(fullname);
	}

	public void resendVerificationMail(UserDetail user, String applicationUrl)
			throws MessagingException, UnsupportedEncodingException {

		LOGGER.info("Inside sendEmail()");
		String mailContent = "<p> Hi, " + user.getFirstName() + " " + user.getLastName() + ", </p>"
				+ "<p>Thank you for registering with us," + ""
				+ "Please, follow the link below to genrate your new verification token.</p>" + "<a href=\""
				+ applicationUrl + "\">Verify your email to activate your account</a>"
				+ "<p> Thank you <br> Users Registration Portal Service";

		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setSubject("New Token generation.");
			helper.setFrom("javalearning1189@gmail.com", "User Registration Portal Service");
			helper.setText(mailContent, true);
			helper.setTo(user.getEmail());
			javaMailSender.send(message);
			LOGGER.info("Email sent successfully", message);
		} catch (Exception e) {
			LOGGER.error("Failed to send email{}", e.getMessage());
		}

	}

	public void resetPasswordMail(UserDetail user, String url) throws MessagingException, UnsupportedEncodingException {

		LOGGER.info("Inside sendEmail()");
		String mailContent = "<p> Hi, " + user.getFirstName() + " " + user.getLastName() + ", </p>"
				+ "<p>Thank you for registering with us," + ""
				+ "Please, follow the link below to genrate your new verification token.</p>" + "<a href=\"" + url
				+ "\">Verify your email to activate your account</a>"
				+ "<p> Thank you <br> Users Registration Portal Service";

		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setSubject("New Token generation.");
			helper.setFrom("javalearning1189@gmail.com", "User Registration Portal Service");
			helper.setText(mailContent, true);
			helper.setTo(user.getEmail());
			javaMailSender.send(message);
			LOGGER.info("Email sent successfully", message);
		} catch (Exception e) {
			LOGGER.error("Failed to send email{}", e.getMessage());
		}

	}

}
