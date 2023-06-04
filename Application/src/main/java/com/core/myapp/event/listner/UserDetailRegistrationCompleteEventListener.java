package com.core.myapp.event.listner;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.core.myapp.event.UserDetailRegistrationEvent;
import com.core.myapp.repo.entity.UserDetail;
import com.core.myapp.service.EmailService;
import com.core.myapp.service.UserService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@RequiredArgsConstructor
public class UserDetailRegistrationCompleteEventListener implements ApplicationListener<UserDetailRegistrationEvent> {

	@Autowired
	private UserService userService;
	
	private  UserDetail theUser;
	@Autowired
	private EmailService emailService;

	
	@Override
	public void onApplicationEvent(UserDetailRegistrationEvent event) {
		 // 1. Get the newly registered user
        theUser = event.getUser();
        //2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        //3. Save the verification token for the user
        userService.saveUserToken(theUser, verificationToken);
        //4 Build the verification url to be sent to the user
        String url = event.getApplicationUrl()+"/register/verifyToken?token="+verificationToken;
        log.info("Click the link to verify your registration :  {}", url);
        //5. Send the email.
        try {
			emailService.sendVerificationMail(url,theUser);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
