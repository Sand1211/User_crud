package com.core.myapp.controller.vo;

import java.time.LocalDateTime;
import java.util.List;

import com.core.myapp.common.ResponseType;
import com.core.myapp.common.VO;
import com.core.myapp.service.EmailService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(value = Include.NON_NULL)
public class ResponseVO {
	private int statusCode;

	@Builder.Default
	private ResponseType responseType = ResponseType.SUCCESS;
	private String message;
	private List<?> payloadList;
	private VO payload;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm a")
	private LocalDateTime ts;
	@JsonInclude(value = Include.CUSTOM,valueFilter = EmailService.class)
	private long totalSize;

}
