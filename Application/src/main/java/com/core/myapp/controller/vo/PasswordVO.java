package com.core.myapp.controller.vo;

import lombok.Data;

@Data
public class PasswordVO {

	private String email;
	private String oldPassword;
	private String newPassword;

}
