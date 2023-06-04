package com.core.myapp.controller;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import com.core.myapp.common.ResponseBuilder;
import com.core.myapp.controller.vo.ResponseVO;
import com.core.myapp.exception.UserAlreadyExistsException;

@RestControllerAdvice
public class BaseController extends ResponseBuilder {

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<ResponseVO> sqlExceptionHandler(SQLException e) {
		ResponseVO responseVO = getErrorResponseVO();
		responseVO.setStatusCode(e.getErrorCode());
		responseVO.setMessage(e.getLocalizedMessage());// "Error during SQL query processing"
		return new ResponseEntity<>(responseVO, HttpStatus.OK);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ResponseVO> noSuchElementExceptionHandler(NoSuchElementException e) {
		ResponseVO responseVO = getErrorResponseVO();
		responseVO.setStatusCode(HttpStatus.NOT_FOUND.value());
		responseVO.setMessage(e.getLocalizedMessage());
		return new ResponseEntity<>(responseVO, HttpStatus.OK);
	}

	@ExceptionHandler(NoSuchFieldError.class)
	public ResponseEntity<ResponseVO> noSuchFieldErrorHandler(NoSuchFieldError e) {
		ResponseVO responseVO = getErrorResponseVO();
		responseVO.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
		responseVO.setMessage(e.getLocalizedMessage());
		return new ResponseEntity<>(responseVO, HttpStatus.OK);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ResponseVO> httpRequestMethodNotSupportedExceptionHandler(
			HttpRequestMethodNotSupportedException e) {
		ResponseVO responseVO = getErrorResponseVO();
		responseVO.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
		responseVO.setMessage(e.getLocalizedMessage());
		return new ResponseEntity<>(responseVO, HttpStatus.OK);
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<ResponseVO> httpClientErrorExceptionHandler(HttpClientErrorException e) {
		ResponseVO responseVO = getErrorResponseVO();
		responseVO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		responseVO.setMessage(e.getLocalizedMessage());
		return new ResponseEntity<>(responseVO, HttpStatus.OK);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ResponseVO> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
		ResponseVO responseVO = getErrorResponseVO();
		responseVO.setStatusCode(HttpStatus.BAD_REQUEST.value());
		responseVO.setMessage(e.getLocalizedMessage());
		return new ResponseEntity<>(responseVO, HttpStatus.OK);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ResponseVO> constraintViolationExceptionHandler(ConstraintViolationException e) {
		ResponseVO responseVO = getErrorResponseVO();
		responseVO.setStatusCode(HttpStatus.CONFLICT.value());
		responseVO.setMessage(e.getLocalizedMessage());
		return new ResponseEntity<>(responseVO, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseVO> handleException(MethodArgumentNotValidException ex) {
		ResponseVO responseVO = getErrorResponseVO();
		responseVO.setStatusCode(ex.getStatusCode().value());
		responseVO.setMessage(ex.getDetailMessageCode());
		return new ResponseEntity<>(responseVO, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ResponseVO> userNotFound(UserAlreadyExistsException ex) {
		ResponseVO responseVO = getErrorResponseVO();
		responseVO.setStatusCode(HttpStatus.BAD_REQUEST.value());
		responseVO.setMessage(ex.getMessage());
		return new ResponseEntity<>(responseVO, HttpStatus.OK);
	}
}
