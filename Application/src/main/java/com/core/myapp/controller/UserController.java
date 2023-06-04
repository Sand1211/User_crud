package com.core.myapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.core.myapp.common.PasswordUtil;
import com.core.myapp.common.Utils;
import com.core.myapp.controller.vo.ResponseVO;
import com.core.myapp.controller.vo.UserVO;
import com.core.myapp.service.EmailService;
import com.core.myapp.service.UserService;

import jakarta.transaction.Transactional;

@RestController
@Transactional
@CrossOrigin
@RequestMapping("/users")
public class UserController extends BaseController {

	@Autowired
	private UserService service;

	@Autowired
	private EmailService emailService;

	@GetMapping("/all")
	public ResponseEntity<ResponseVO> getAll() {
		List<UserVO> all = service.getAll();
		return buildResponse(all, all.size());
	}

	@PostMapping("/saveuser")
	public ResponseEntity<ResponseVO> saveUser(@RequestBody UserVO userVO) {
		boolean exist = service.isEmailExist(userVO);
		if (exist) {
			return buildErrorResponse("Email Already exist.");
		}
		if (!Utils.isNull(userVO.getUserId())) {
			if (service.isIdExist(userVO.getUserId())) {
				service.update(userVO);

				if (userVO.isEmailChanged()) {
					emailService.sendMail(userVO);
				}
				return buildResponse("User updated successfully with userId " + userVO.getUserId() + "");
			} else {
				return buildErrorResponse("user id not exist");
			}
		}
		generatePassword(userVO);
		service.save(userVO);
		emailService.sendMail(userVO);
		return buildResponse("User created successfully");
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseVO> getUserById(@PathVariable Integer id) {
		if (service.isIdExist(id)) {
			UserVO vo = service.getById(id);
			return buildResponse(vo);

		} else {
			return buildErrorResponse("User Not Exist");
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseVO> deleteUser(@PathVariable Integer id) {
		if (service.isIdExist(id)) {
			if (service.deletUser(id)) {
				return buildResponse("User Deactivated with id " + id + "");
			} else {
				return buildErrorResponse("User with id " + id + " Already Deactivated");
			}
		} else {
			return buildErrorResponse("User Not Exist");
		}
	}

	private void generatePassword(UserVO userVO) {
		String genPwd = PasswordUtil.generatePasssword(8);
		userVO.setPassword(genPwd);
	}

	@PostMapping("/{id}")
	public ResponseEntity<ResponseVO> activateUser(@PathVariable Integer id) {
		if (service.isIdExist(id)) {
			if (service.activateUser(id)) {
				return buildResponse("User Activated with id " + id + "");
			} else {
				return buildErrorResponse("User with id " + id + " Already Activated");
			}
		} else {
			return buildErrorResponse("User Not Exist");
		}
	}

}
