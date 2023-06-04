package com.core.myapp.controller.vo;

import com.core.myapp.common.Utils;
import com.core.myapp.common.VO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class UserVO implements VO {

	private Integer userId;
	private String userName;
	private String status;
	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private String email;
	private String phone;
	private String password;
	private String fullName;
	private String previousEmail;
	private String role;
	
	public boolean isEmailChanged() {
		if (!Utils.isEmpty(email) && !(Utils.isEmpty(previousEmail))) {
			return !(previousEmail.equalsIgnoreCase(email));
		}
		return false;
	}

}
