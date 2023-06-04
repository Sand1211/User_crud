package com.core.myapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.myapp.controller.vo.LoginVo;
import com.core.myapp.repo.UserRepository;
import com.core.myapp.repo.entity.UserDetail;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class LoginService {

	@Autowired
	private UserRepository repository;

	public boolean login(LoginVo loginVo) {
		boolean retValue;
//		UserDetail findByUserName = repository.findByUserName(loginVo.getUname());
//		if (loginVo.getPwd().equals(findByUserName.getPassword())) {
//			retValue = true;
//		} else {
//			retValue = false;
//		}

		return true;
	}

}
