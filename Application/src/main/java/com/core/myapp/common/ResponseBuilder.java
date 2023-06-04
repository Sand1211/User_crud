package com.core.myapp.common;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.core.myapp.controller.vo.ResponseVO;


public class ResponseBuilder {

	protected ResponseEntity<ResponseVO> buildResponse(List<?> voList, long size) {
		ResponseVO responseVO = ResponseVO.builder().statusCode(1).ts(LocalDateTime.now()).payloadList(voList).totalSize(size)
				.build();
		responseVO.setPayloadList(voList);
		return new ResponseEntity<>(responseVO, HttpStatus.OK);
	}

	protected ResponseEntity<ResponseVO> buildResponse(VO vo) {
		ResponseVO responseVO = ResponseVO.builder().statusCode(1).ts(LocalDateTime.now()).payload(vo).build();
		responseVO.setPayload(vo);
		return new ResponseEntity<>(responseVO, HttpStatus.OK);
	}
	protected ResponseEntity<ResponseVO> buildResponse(String message) {
		ResponseVO responseVO = ResponseVO.builder().statusCode(1).ts(LocalDateTime.now()).responseType(ResponseType.SUCCESS)
				.message(message).build();
		return new ResponseEntity<>(responseVO, HttpStatus.OK);
	}

	protected ResponseEntity<ResponseVO> buildErrorResponse(String message) {
		ResponseVO responseVO = ResponseVO.builder().statusCode(0).ts(LocalDateTime.now()).responseType(ResponseType.FAIL)
				.message(message).build();
		return new ResponseEntity<>(responseVO, HttpStatus.OK);
	}

	protected ResponseVO getErrorResponseVO() {
		return ResponseVO.builder().statusCode(0).ts(LocalDateTime.now()).responseType(ResponseType.ERROR).build();
	}

}
