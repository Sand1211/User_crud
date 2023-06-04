package com.core.myapp.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.core.myapp.common.Utils;
import com.core.myapp.controller.vo.UserRegistrationVO;
import com.core.myapp.controller.vo.UserVO;
import com.core.myapp.exception.UserAlreadyExistsException;
import com.core.myapp.repo.ResetPasswordRepository;
import com.core.myapp.repo.TokenRepository;
import com.core.myapp.repo.UserRepository;
import com.core.myapp.repo.entity.ResetPassword;
import com.core.myapp.repo.entity.Token;
import com.core.myapp.repo.entity.UserDetail;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private static final Logger LOGGER = LogManager.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenRepository tokenRepository;
	private final ResetPasswordRepository resetPasswordRepository;

	public List<UserVO> getAll() {
		List<UserDetail> findAll = userRepository.findAll();
		List<UserVO> userVO = new ArrayList<>();
		for (UserDetail user : findAll) {
			UserVO vo = new UserVO();
			vo.setUserId(user.getUserId());
			vo.setStatus(user.isStatus() ? "Active" : "Inactive");
			vo.setEmail(user.getEmail());
			vo.setFirstName(user.getFirstName());
			vo.setLastName(user.getLastName());
			vo.setPhone(user.getPhone());
			vo.setRole(user.getRole());
			userVO.add(vo);
		}
		return userVO;
	}

	public void save(UserVO vo) {
		UserDetail detail = new UserDetail();
		detail.setEmail(vo.getEmail());
		detail.setFirstName(vo.getFirstName());
		detail.setLastName(vo.getLastName());
		detail.setPhone(vo.getPhone());
		detail.setPassword(vo.getPassword());
		detail.setRole("USER");
		userRepository.save(detail);
	}

	public boolean isEmailExist(UserVO vo) {
		return userRepository.existsByEmailAndStatus(vo.getEmail(), true);
	}

	public void update(UserVO vo) {
		UserDetail detail = new UserDetail();
		detail.setUserId(vo.getUserId());
		detail.setEmail(vo.getEmail());
		detail.setFirstName(vo.getFirstName());
		detail.setLastName(vo.getLastName());
		detail.setPhone(vo.getPhone());
		userRepository.save(detail);
	}

	public UserVO getById(Integer id) {
		UserDetail user = userRepository.findById(id).get();
		UserVO vo = new UserVO();
		vo.setUserId(user.getUserId());
		vo.setStatus(user.isStatus() ? "Active" : "Inactive");
		vo.setEmail(user.getEmail());
		vo.setFirstName(user.getFirstName());
		vo.setLastName(user.getLastName());
		vo.setPhone(user.getPhone());
		return vo;
	}

	public boolean deletUser(Integer userId) {
		boolean retValue;
		UserDetail user = userRepository.findById(userId).get();
		if (user.isStatus()) {
			userRepository.deleteById(user.getUserId());
			retValue = true;
		} else {
			retValue = false;
		}
		return retValue;
	}

	public boolean activateUser(Integer userId) {
		boolean retValue;
		UserDetail user = userRepository.findById(userId).get();
		if (!user.isStatus()) {
			userRepository.activateById(user.getUserId());
			retValue = true;
		} else {
			retValue = false;
		}
		return retValue;
	}

	public boolean isUserExist(String email) {
		boolean existsByEmail = userRepository.existsByEmail(email);
		if (existsByEmail) {
			return true;
		}
		return false;
	}

	public boolean isIdExist(Integer id) {
		LOGGER.info("Checking userId ", id);
		boolean existsByUserId = userRepository.existsByUserId(id);
		if (existsByUserId) {
			return true;
		} else
			LOGGER.warn("UserId not  exist => {}", id);
		return false;
	}

	public UserDetail registerUser(UserRegistrationVO request) {
		UserDetail findByEmail = userRepository.findByEmail(request.getEmail());
		if (!Utils.isNull(findByEmail)) {
			throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
		}
		UserDetail newUser = new UserDetail();
		newUser.setFirstName(request.getFirstName());
		newUser.setLastName(request.getLastName());
		newUser.setPhone(request.getPhone());
		newUser.setEmail(request.getEmail());
		newUser.setPassword(passwordEncoder.encode(request.getPassword()));
		newUser.setRole(request.getRole());
		return userRepository.save(newUser);
	}

	public UserDetail findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void saveUserToken(UserDetail user, String token) {
		Token t = new Token(token, user);
		tokenRepository.save(t);
	}

	public boolean setEnabledToken(String theToken) {
		Token token = findByToken(theToken);
		UserDetail user = token.getUser();
		Calendar calendar = Calendar.getInstance();
		if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
			tokenRepository.updateStatus(token.getToken());
			return false;
		}
		user.setEnabled(true);
		userRepository.save(user);
		return true;
	}

	public Token findByToken(String token) {
		return tokenRepository.findByToken(token);
	}

	public ResetPassword findByTokenInResetPass(String token) {
		return resetPasswordRepository.findByToken(token);
	}

	public UserDetail findUserByToken(String token) {
		Token findByToken = tokenRepository.findByToken(token);
		return findByToken.getUser();
	}

	public Token generateNewToken(String oldToken) {
		Token findByToken = findByToken(oldToken);
		Calendar calendar = Calendar.getInstance();
		if ((findByToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
			findByToken.setToken(UUID.randomUUID().toString());
			findByToken.setExpirationTime(findByToken.getTokenExpirationTime());
			findByToken.setStatus(true);
			tokenRepository.save(findByToken);
			return findByToken;
		}
		return findByToken;
	}

	public void createPasswordResetTokenForUser(String token, UserDetail findByEmail) {
		ResetPassword password = new ResetPassword(token, findByEmail);
		resetPasswordRepository.save(password);
	}

	public boolean savePasswordToken(String token) {
		ResetPassword rp = resetPasswordRepository.findByToken(token);
		Calendar calendar = Calendar.getInstance();
		if ((rp.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
			resetPasswordRepository.updateStatus(rp.getToken());
			return false;
		}
		return true;
	}

	public Optional<UserDetail> getUserByPasswordRestToken(String token) {
		return Optional.ofNullable(resetPasswordRepository.findByToken(token).getUser());

	}

	public void changePassword(UserDetail userDetail, String newPassword) {
		userDetail.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(userDetail);

	}

}
