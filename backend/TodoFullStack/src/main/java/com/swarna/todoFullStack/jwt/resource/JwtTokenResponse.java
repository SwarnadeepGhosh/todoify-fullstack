package com.swarna.todoFullStack.jwt.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class JwtTokenResponse implements Serializable {

	private static final long serialVersionUID = 8317676219297719109L;
	private final String token;
}