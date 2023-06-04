package com.core.myapp.event;

import org.springframework.context.ApplicationEvent;

import com.core.myapp.repo.entity.UserDetail;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserDetailRegistrationEvent extends ApplicationEvent {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private UserDetail user;
	private String applicationUrl;

	public UserDetailRegistrationEvent(UserDetail user, String applicationUrl) {
		super(user);
		this.user = user;
		this.applicationUrl = applicationUrl;
	}
}
