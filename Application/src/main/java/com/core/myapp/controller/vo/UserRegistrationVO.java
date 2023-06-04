package com.core.myapp.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(value = Include.NON_NULL)
public class UserRegistrationVO {

	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String role;
	private String phone;

}
