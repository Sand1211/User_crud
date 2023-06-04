package com.core.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.core.myapp.controller.vo.ResponseVO;
import com.core.myapp.controller.vo.UserVO;
import com.core.myapp.service.EmailService;

import jakarta.mail.MessagingException;

@RestController
@CrossOrigin
@RequestMapping("/email")
public class EmailController extends BaseController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/sendEmail")
	public ResponseEntity<ResponseVO> sendmail(@RequestBody UserVO vo) throws MessagingException {
		boolean sendMail = emailService.sendMail(vo);
		if (sendMail) {
			return buildResponse("Email sent Successfully");
		} else {
			return buildErrorResponse("Failed to send email");
		}
	}

}
