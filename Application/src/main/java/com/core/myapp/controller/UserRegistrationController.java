package com.core.myapp.controller;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.myapp.controller.vo.PasswordVO;
import com.core.myapp.controller.vo.ResponseVO;
import com.core.myapp.controller.vo.UserRegistrationVO;
import com.core.myapp.event.UserDetailRegistrationEvent;
import com.core.myapp.repo.entity.ResetPassword;
import com.core.myapp.repo.entity.Token;
import com.core.myapp.repo.entity.UserDetail;
import com.core.myapp.service.EmailService;
import com.core.myapp.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
@Transactional
@Slf4j
public class UserRegistrationController extends BaseController {

	private final UserService userService;
	private final ApplicationEventPublisher publisher;
	private final EmailService emailService;

	@PostMapping
	public ResponseEntity<ResponseVO> registerUser(@RequestBody UserRegistrationVO vo,
			final HttpServletRequest request) {
		UserDetail user = userService.registerUser(vo);
		publisher.publishEvent(new UserDetailRegistrationEvent(user, applicationUrl(request)));
		return buildResponse("Success!  Please, check your email to complete your registration");
	}

	@GetMapping("/verifyToken")
	public ResponseEntity<ResponseVO> verifyToken(@RequestParam("token") String token) {
		Token findByToken = userService.findByToken(token);
		if (findByToken == null) {
			return buildErrorResponse("Invalid Token..");
		}
		Calendar calendar = Calendar.getInstance();
		if (!(findByToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0)) {
			return buildResponse("Token Already Verified!! Please login...");
		}

		if (userService.setEnabledToken(token)) {
			return buildResponse("Token Verified Succesfully..");
		} else {
			return buildErrorResponse("Token expired");
		}

	}

	@GetMapping("/resendToken")
	public ResponseEntity<ResponseVO> resendToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
		Token findByToken = userService.findByToken(oldToken);
		if (findByToken == null) {
			return buildErrorResponse("Invalid Token..");
		}
		Calendar calendar = Calendar.getInstance();
		if (!(findByToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0)) {
			return buildErrorResponse("Token is not expired..and will expire in ");
		}
		Token token = userService.generateNewToken(oldToken);
		UserDetail user = token.getUser();
		resendMailForNewToken(user, applicationUrl(request), token);
		return buildResponse("New Token has been sent to Your Registered email address..");
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<ResponseVO> forgotPassword(@RequestBody PasswordVO passwordVO, HttpServletRequest request) {

		UserDetail user = userService.findByEmail(passwordVO.getEmail());
		if (user == null) {
			return buildErrorResponse("User not found...");
		}
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(token, user);
		sendMailForSavePasswordToken(user, token, applicationUrl(request));
		return buildResponse("Email send to your registered email id to reset your Password..");

	}

	@PostMapping("/savePassword")
	public ResponseEntity<ResponseVO> savePassword(@RequestBody PasswordVO passwordVO,
			@RequestParam("token") String token) {
		ResetPassword rsp = userService.findByTokenInResetPass(token);
		if (rsp == null) {
			return buildErrorResponse("Invalid Reset Token...");
		}
		if (!userService.savePasswordToken(token)) {
			return buildErrorResponse("Token is expired or invalid..");
		}
		Optional<UserDetail> user = userService.getUserByPasswordRestToken(token);
		if (!user.isPresent()) {
			return buildErrorResponse("User not available with this token..!!");
		}
		userService.changePassword(user.get(), passwordVO.getNewPassword());
		return buildResponse("Password Changed Successfully");

	}

	private void sendMailForSavePasswordToken(UserDetail findByEmail, String token, String applicationUrl) {
		String url = applicationUrl + "/register/savePassword?token=" + token;
		log.info("Click the link to reset your password :  {}", url);
		try {
			emailService.resetPasswordMail(findByEmail, url);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void resendMailForNewToken(UserDetail user, String applicationUrl, Token token) {
		String url = applicationUrl + "/register/verifyToken?token=" + token.getToken();
		log.info("Click the link to resent token :  {}", url);
		try {
			emailService.resendVerificationMail(user, url);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String applicationUrl(HttpServletRequest request) {

		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

}
