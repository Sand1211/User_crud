package com.core.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.core.myapp.controller.vo.LoginVo;
import com.core.myapp.controller.vo.ResponseVO;
import com.core.myapp.repo.entity.UserDetail;
import com.core.myapp.service.LoginService;
import com.core.myapp.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/myapp/api/login")
public class LoginController extends BaseController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private UserService userService;

	@PostMapping("/loginUser")
	public ResponseEntity<ResponseVO> login(@RequestBody LoginVo loginVo) {
		boolean userExist = userService.isUserExist(loginVo.getUname());
		if (userExist) {
			
				boolean login = loginService.login(loginVo);
				if (login) {
					return buildResponse("Login Successfull..");
				} else {
					return buildErrorResponse("Password not matched..");
				}
			
		}
		return buildErrorResponse("User not Exist..");
	}
}
